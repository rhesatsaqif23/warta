# Agent Skills

This project uses two sets of agent skills for AI-assisted development.

## Project-Specific Skills (`docs/skills/`)

Custom skills tailored for the Warta app architecture:

| Skill | Purpose |
|-------|---------|
| `clean-architecture-feature` | Scaffolding new features with Clean Architecture |
| `compose-design-system` | Material 3 styling and shared components |
| `mvvm-layer-boundaries` | ViewModel → UseCase → Repository rule |
| `offline-first-room` | Room persistence and offline-first strategy |
| `git-conventional-commits` | Git workflow and commit message style |
| `testing-quality` | Testing strategy and quality checks |

## General Android Skills (`.github/skills/`)

Standard Android development skills from [awesome-android-agent-skills](https://github.com/new-silvermoon/awesome-android-agent-skills):

| Category | Skills |
|----------|--------|
| Architecture | `android-architecture`, `android-data-layer`, `android-viewmodel` |
| UI | `compose-ui`, `compose-navigation`, `coil-compose`, `android-accessibility` |
| Concurrency | `android-coroutines`, `android-retrofit`, `kotlin-concurrency-expert` |
| Performance | `compose-performance-audit`, `gradle-build-performance` |
| Testing | `android-testing`, `android-emulator-skill` |
| Migration | `xml-to-compose-migration`, `rxjava-to-coroutines-migration` |
| Build | `android-gradle-logic` |

## Usage

AI agents automatically discover and load relevant skills when working on tasks. See `Agent.md` for project conventions.
