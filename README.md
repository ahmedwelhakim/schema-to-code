# Schema to Code

![Build](https://github.com/ahmedwelhakim/schema-to-code/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

An IntelliJ IDEA plugin that generates type-safe code from JSON data. Paste your JSON and instantly get TypeScript
interfaces, type aliases, and more.

## Features

- **JSON to TypeScript**: Automatically infer types from JSON data
- **Multiple Output Modes**: Generate separate interfaces or nested inline types
- **Naming Strategies**: Support for camelCase, PascalCase, snake_case, or preserve original names
- **Type Merging**: Intelligently merge similar object structures
- **Real-time Preview**: See generated code as you type
- **Customizable Options**: Configure output format per language

## Architecture

The project follows a clean multi-module architecture:

```
schema-to-code/
├── core/                    # Platform-independent business logic
│   └── src/main/kotlin/
│       └── .../core/
│           ├── config/      # Configuration classes (TargetLanguage, InputFormat, etc.)
│           ├── emit/        # Code emitters (TypeScript, etc.)
│           ├── i18n/        # Internationalization support
│           ├── infer/       # Input parsing (JSON inference)
│           ├── ir/          # Intermediate representation (TypeDef, Field, etc.)
│           ├── language/    # Language descriptors
│           ├── naming/      # Naming strategies (camelCase, PascalCase, etc.)
│           ├── normalize/   # Type normalization and merging
│           ├── options/     # Option definitions
│           ├── resolve/     # Type name resolution
│           ├── result/      # Result types for error handling
│           ├── service/     # Main service (SchemaToCodeService)
│           └── util/        # Utility functions
│
└── plugin/                  # IntelliJ IDEA plugin
    └── src/main/kotlin/
        └── .../plugin/
            ├── language/    # Language registry and IDs
            ├── state/       # Persistent settings
            ├── ui/          # UI components (panels, editors)
            ├── util/        # UI utilities
            └── viewmodel/   # MVVM view models
```

### Processing Pipeline

```
JSON Input → Parse (Infer) → Normalize → Plan → Emit → TypeScript Output
```

1. **Infer**: Parse JSON and create intermediate representation (IR)
2. **Normalize**: Merge structurally equivalent types
3. **Plan**: Allocate unique names and collect type declarations
4. **Emit**: Generate target language code

<!-- Plugin description -->
**Schema to Code** converts JSON data into type-safe code instantly.

Features:

- Generate TypeScript interfaces or type aliases from JSON
- Multiple naming strategies (camelCase, PascalCase, snake_case)
- Separate or nested type emission modes
- Real-time code generation as you type

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  schema-to-code"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/ahmedwelhakim/schema-to-code/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

1. Open the **Schema to Code** tool window (View → Tool Windows → Schema to Code)
2. Paste your JSON in the left editor
3. Configure options (language, naming strategy, emission mode)
4. Copy the generated code from the right editor

## Building

```bash
./gradlew build
```

## Testing

```bash
./gradlew test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License.

## TODO

- [x] Support TypeScript code generation
- [x] Support JSON input parsing and inference
- [x] Support multiple naming strategies (camelCase, PascalCase, snake_case, preserve)
- [x] Support separate and nested type emission modes
- [x] Real-time code generation and preview in IntelliJ
- [x] Customizable options per language
- [ ] Support C# code generation
- [ ] Support Kotlin code generation
- [ ] Support Java code generation
- [ ] Support reading and generating code from OpenAPI schema
- [ ] Support reading and generating code from Swagger schema

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
