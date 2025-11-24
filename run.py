#!/usr/bin/env python3
"""
Script para automatizar build e execução do compilador
Uso: python run.py [--build] [--tokens] [--ast] [--run] arquivo.prog
python run.py tests/(nome)
"""

import os
import sys
import subprocess
import argparse
from pathlib import Path

def run_command(cmd, description):
    """Executa comando e exibe resultado"""
    print(f"[INFO] {description}")
    print(f"[CMD] {cmd}")
    
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    
    if result.stdout:
        print(result.stdout)
    if result.stderr:
        print(result.stderr)
    
    if result.returncode != 0:
        print(f"[ERRO] Comando falhou com código {result.returncode}")
        sys.exit(1)
    
    print()

def build():
    """Regenera lexer, parser e compila tudo"""
    print("=== BUILD ===")
    
    # JFlex - gera MiniLexer.java
    run_command(
        "java -jar lib/jflex-1.8.2.jar -d src/compilador/lexer src/compilador/lexer/MiniLexer.lex",
        "Gerando MiniLexer.java com JFlex"
    )
    
    # CUP - gera Parser.java e sym.java
    run_command(
        "java -jar lib/java-cup-11b.jar -parser Parser -symbols sym -destdir src/compilador/parser src/compilador/parser/parser.cup",
        "Gerando Parser.java e sym.java com CUP"
    )
    
    # Compilação
    separator = ";" if os.name == "nt" else ":"
    run_command(
        f'javac -cp ".{separator}lib/java-cup-11b-runtime.jar" -d bin src/compilador/lexer/MiniLexer.java src/compilador/parser/Parser.java src/compilador/parser/sym.java src/compilador/parser/*.java src/compilador/semantic/*.java src/compilador/exec/*.java src/compilador/main/*.java',
        "Compilando todas as classes Java"
    )
    
    print("Build concluído com sucesso!")

def run_compiler(filename, mode_tokens=False, mode_ast=False, mode_run=True):
    """Executa o compilador"""
    if not os.path.exists(filename):
        print(f"[ERRO] Arquivo não encontrado: {filename}")
        sys.exit(1)
    
    # Monta argumentos
    args = []
    if mode_tokens:
        args.append("--tokens")
    if mode_ast:
        args.append("--ast")
    if mode_run:
        args.append("--run")
    
    args.append(filename)
    args_str = " ".join(args)
    
    # Executa
    separator = ";" if os.name == "nt" else ":"
    run_command(
        f'java -cp "bin{separator}lib/java-cup-11b-runtime.jar" compilador.main.Main {args_str}',
        f"Executando compilador: {filename}"
    )

    if mode_ast | mode_run:
        run_command(
            cmd=f"dot -Tpng ast.dot -o {filename}_ast.png",
            description=f"Gerando {filename}_ast.png se ast for válida!"
        )

def check_files():
    """Verifica se arquivos necessários existem"""
    required_files = [
        "lib/jflex-1.8.2.jar",
        "lib/java-cup-11b.jar", 
        "lib/java-cup-11b-runtime.jar",
        "src/compilador/lexer/MiniLexer.lex",
        "src/compilador/parser/parser.cup"
    ]
    
    missing = []
    for file in required_files:
        if not os.path.exists(file):
            missing.append(file)
    
    if missing:
        print("[ERRO] Arquivos necessários não encontrados:")
        for file in missing:
            print(f"  - {file}")
        sys.exit(1)

def main():
    parser = argparse.ArgumentParser(description="Automatiza build e execução do compilador")
    parser.add_argument("--build", action="store_true", help="Força rebuild (JFlex + CUP + javac)")
    parser.add_argument("--tokens", action="store_true", help="Exibe tokens do lexer")
    parser.add_argument("--ast", action="store_true", help="Exibe AST")
    parser.add_argument("--run", action="store_true", help="Executa o programa")
    parser.add_argument("arquivo", nargs="?", help="Arquivo .prog para processar")
    
    args = parser.parse_args()
    
    # Verifica arquivos necessários
    check_files()
    
    # Se não especificou modos, assume --run
    if not args.tokens and not args.ast and not args.run:
        args.run = True
    
    # Build forçado ou se não existem classes compiladas
    if args.build or not os.path.exists("bin/compilador"):
        build()
    
    # Se especificou arquivo, executa
    if args.arquivo:
        run_compiler(args.arquivo, args.tokens, args.ast, args.run)
    else:
        print("Build concluído. Use: python run.py tests\\arquivo.prog")

if __name__ == "__main__":
    main()