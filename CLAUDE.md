# Project Instructions for AI Agents

## Ground Rules

- Never assume you are the only one working on this project.
- Do not begin work on new features if the build is broken.
- Do not begin work on new features if tests are not passing.
- Do not begin work on new features if the linter is not passing.
- If you are unsure what to do (e.g. can't see the result of a build), stop and ask.

<!-- BEGIN BEADS INTEGRATION v:1 profile:minimal hash:ca08a54f -->
## Beads Issue Tracker

This project uses **bd (beads)** for issue tracking. Run `bd prime` to see full workflow context and commands.

### Quick Reference

```bash
bd ready              # Find available work
bd show <id>          # View issue details
bd update <id> --claim  # Claim work
bd close <id>         # Complete work
```

### Rules

- Use `bd` for ALL task tracking — do NOT use TodoWrite, TaskCreate, or markdown TODO lists
- Run `bd prime` for detailed command reference and session close protocol
- Use `bd remember` for persistent knowledge — do NOT use MEMORY.md files

## Session Completion

**When ending a work session**, you MUST complete ALL steps below. Work is NOT complete until `git push` succeeds.

**MANDATORY WORKFLOW:**

1. **File issues for remaining work** - Create issues for anything that needs follow-up
2. **Run quality gates** (if code changed) - Tests, linters, builds
3. **Update issue status** - Close finished work, update in-progress items
4. **PUSH TO REMOTE** - This is MANDATORY:
   ```bash
   git pull --rebase
   bd dolt push
   git push
   git status  # MUST show "up to date with origin"
   ```
5. **Clean up** - Clear stashes, prune remote branches
6. **Verify** - All changes committed AND pushed
7. **Hand off** - Provide context for next session

**CRITICAL RULES:**
- Work is NOT complete until `git push` succeeds
- NEVER stop before pushing - that leaves work stranded locally
- NEVER say "ready to push when you are" - YOU must push
- If push fails, resolve and retry until it succeeds
<!-- END BEADS INTEGRATION -->


## Build & Test

```bash
./gradlew :app:assembleDebug       # Build Wear OS watch app
./gradlew :mobile:assembleDebug    # Build companion phone app
./gradlew :app:bundleRelease       # Release AAB (needs KEYSTORE_PATH/PASSWORD/KEY_ALIAS/KEY_PASSWORD env vars)
./gradlew :mobile:bundleRelease
./gradlew testDebugUnitTest        # Unit tests (JUnit 5 / MockK)
./gradlew lintDebug                # Android lint
./gradlew ktlintCheck              # Code style
./gradlew ktlintFormat             # Auto-fix style
```

## Architecture Overview

DashCode is a **Wear OS QR catalog** with a minimal **companion phone app**.

```
app/          — Wear OS watch app (Pixel Watch)
  data/       — Room DB: QrCode entity (id, name, content)
  sync/       — WearableListenerService: receives JSON from phone, replaces DB
  ui/list/    — ScalingLazyColumn list of code names
  ui/detail/  — Full-screen QR bitmap; FLAG_KEEP_SCREEN_ON
  util/       — QrBitmapGenerator (ZXing → Bitmap)

mobile/       — Companion phone app
  data/       — DataStore: stores codes as JSON list
  ui/         — LazyColumn + FAB add dialog; Sync button → DataClient.putDataItem
```

Both share `applicationId = "com.vernonit.dashcode"` so the Wearable Data Layer pairs them automatically.

CI/CD: GitHub Actions → `assembleDebug` on PRs; `bundleRelease` + Play Store upload (internal track) on push to `main`.

## Conventions & Patterns

- Kotlin + Jetpack Compose throughout (Wear Compose for watch, Material3 for phone)
- Hilt for DI; KSP for annotation processing
- `wear-compose-material3:1.5.0` — all APIs stable, **no `ExperimentalWearComposeMaterial3Api` opt-in needed**
- `material3:1.4.0` — `TopAppBar` is experimental, requires `@OptIn(ExperimentalMaterial3Api::class)`
- `ExperimentalWearFoundationApi` still required for `ScalingLazyColumn` / `ExperimentalWearFoundationApi`
- No comments unless the WHY is non-obvious; no docstrings
