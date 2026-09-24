---
name: git-conventional-commits
description: |
  Enforce the repo's git workflow and commit message style. Use when working
  on branches, proposing commit messages, creating pull requests, rebasing,
  or pushing. Trigger on "commit", "branch", "pull request", "conventional
  commits", "push", "rebase".
tools:
  - filesystem
resources: []
---

# Git / Conventional Commits — Warta

Codified workflow for version control in this repo.

## Branching

- Use the short `feat/<name>` prefix — e.g. `feat/home-screen`,
  `feat/search-feature`, `feat/detail-page`.
- `fix/<name>`, `refactor/<name>` for non-feature work.
- Do **not** push directly to `main`. Use pull requests for review.

## Commit message format

```
<type>[optional scope]: <description>

- <discrete change>
- <discrete change>
```

- **type**: `feat:` new feature · `fix:` bug fix · `docs:`/`refactor:`/`chore:`/
  `test:`/`perf:` as appropriate.
- **scope** (optional): a noun in parentheses, e.g. `feat(news):`.
- **body**: one blank line after the description; preferred style uses `- `
  bullets listing each discrete change.

Examples:

```
feat: implement home screen with headline news

- add NewsCard composable
- wire HomeViewModel via hiltViewModel()
- add loading/empty/error states
```

```
fix: handle empty news response gracefully
```

## Proposing a commit message

At the end of every task that changes code or docs, propose a commit message
for that change using Conventional Commits. **Do not commit unless explicitly
asked.** Never commit secrets.

## Checklist
- [ ] Branch is `feat/<name>` (or a `fix/`/`refactor/` branch), not `main`
- [ ] Commit message uses `type[scope]: description`
- [ ] Body uses `- ` bullets for discrete changes
- [ ] No secrets in the commit
- [ ] Only committed when explicitly asked
