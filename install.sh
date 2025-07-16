#!/bin/bash

set -e

check_java() {
    if command -v java &> /dev/null; then
        echo "Java is already installed."
        java -version
        exit 0
    fi
}

check_java

OS="$(uname -s)"

case "$OS" in
    Darwin)
        echo "Detected macOS."
        if ! command -v brew &> /dev/null; then
            echo "Homebrew is not installed. Please install Homebrew first: https://brew.sh/"
            exit 1
        fi
        echo "Installing OpenJDK via Homebrew..."
        brew install openjdk || true
        if ! grep -q '/opt/homebrew/opt/openjdk/bin' ~/.zshrc 2>/dev/null; then
            echo 'export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"' >> ~/.zshrc
            export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
        fi
        echo "Java installation complete."
        ;;
    Linux)
        echo "Detected Linux."
        if command -v apt &> /dev/null; then
            sudo apt update
            sudo apt install -y openjdk-11-jdk
        elif command -v yum &> /dev/null; then
            sudo yum install -y java-11-openjdk-devel
        else
            echo "Unsupported Linux package manager. Please install OpenJDK manually."
            exit 1
        fi
        echo "Java installation complete."
        ;;
    MINGW*|MSYS*|CYGWIN*)
        echo "Detected Windows (via Git Bash, MSYS, or Cygwin)."
        echo "Please download and install OpenJDK from: https://adoptium.net/ or https://jdk.java.net/"
        echo "After installation, restart your terminal and ensure 'java' is in your PATH."
        ;;
    *)
        echo "Unknown OS: $OS. Please install Java manually."
        exit 1
        ;;
esac

check_java 