# Claude Code Project Instructions

## Development Standards
This project follows our team's development standards. Please review and adhere to these standards for all code changes.

**Standards Document**: [Link to your standards - e.g., https://github.com/vidv13/development-standards/blob/main/DEVELOPMENT_STANDARDS.md]

## Core Practices

### Trunk-Based Development
- Work on `main` branch or create short-lived feature branches (max 1-2 days)
- Integrate code to main at least daily
- Delete feature branches immediately after merging
- Use feature flags for incomplete features, not long-lived branches

### Acceptance Test-Driven Development (ATDD)
Before implementing any feature:
1. Confirm acceptance criteria are defined
2. Write acceptance tests in `tests/acceptance/`
3. Implement feature to make tests pass
4. Refactor while keeping tests green

**Test Structure**: Use Given-When-Then format
```gherkin
Given [initial context]
When [event occurs]
Then [expected outcome]
```

### Testing Requirements
- Write acceptance tests for all new features
- Maintain minimum 80% code coverage
- Follow the testing pyramid: 70% unit, 20% integration, 10% acceptance/e2e
- All tests must pass before committing
- No flaky tests - fix or delete immediately

## Code Quality Expectations

### When Writing Code
- Follow SOLID principles
- Keep functions small and focused
- Use clear, descriptive names (self-documenting code)
- Extract common patterns (DRY)
- Prefer simple solutions (KISS)
- Don't build speculative features (YAGNI)

### File Organization
```
src/
├── features/        # Feature modules
├── components/      # Reusable components
└── utils/          # Utility functions

tests/
├── acceptance/     # End-to-end acceptance tests
├── integration/    # Integration tests
└── unit/          # Unit tests
```

### Documentation
- Update README.md if adding new setup steps
- Add inline comments only for complex logic
- Keep code self-documenting through clear naming
- Document public APIs
- Create ADRs (Architecture Decision Records) for significant decisions in `docs/adr/`

## Code Review Standards
All code will be reviewed. Please ensure:
- [ ] Tests exist and pass
- [ ] Code follows standards above
- [ ] Acceptance criteria are met
- [ ] No security vulnerabilities introduced
- [ ] Code coverage meets 80% minimum

## Security Requirements
- Never commit secrets, API keys, or credentials
- Use environment variables for configuration
- Validate all user inputs
- Sanitize data before database queries
- Keep dependencies updated

## Performance Considerations
- Optimize database queries (watch for N+1 problems)
- Implement caching where appropriate
- Keep API response times under 200ms (p95)
- Profile code if performance is critical

## Accessibility
- Use semantic HTML
- Ensure keyboard navigation works
- Test with screen readers when modifying UI
- Maintain WCAG 2.1 Level AA compliance

## Before Committing
Run this checklist:
- [ ] All tests pass (`npm test` or equivalent)
- [ ] Code is linted (`npm run lint` or equivalent)
- [ ] Acceptance tests cover new functionality
- [ ] No console.log or debug statements left in code
- [ ] No commented-out code blocks
- [ ] Commit message is clear and descriptive

## Technology-Specific Guidelines

### Kotlin / Android / Wear OS
**Naming Conventions**:
- Variables and functions: `camelCase`
- Classes, interfaces, objects: `PascalCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: `lowercase.dot.separated`
- Files: `PascalCase.kt` matching the primary class name

**Preferred Patterns**:
- Use coroutines/Flow over callbacks
- Prefer `val` over `var`, immutable data classes
- Use Jetpack Compose for all UI (no XML layouts)
- Use Hilt for dependency injection with KSP (not KAPT)
- Use sealed classes/interfaces for state modeling
- Leverage Kotlin scope functions (`let`, `run`, `apply`, `also`) appropriately

**Architecture**:
- Package by feature under `com.vidv13.dashcode`
- UI layer: Compose screens + ViewModels
- Data layer: Repository pattern with Room + DataStore
- DI: Hilt modules per feature

## Common Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests (JUnit 5 + MockK)
./gradlew testDebugUnitTest

# Run lint checks
./gradlew lintDebug

# Run all checks
./gradlew assembleDebug testDebugUnitTest lintDebug

# Clean build
./gradlew clean

# Install on connected watch/emulator
./gradlew installDebug
```

## Questions?
If you're unsure about how to implement something or need clarification on standards:
1. Check the full standards document (link above)
2. Look at existing code in the project for examples
3. Ask in team chat or code review

## Project-Specific Notes
<!-- Add any project-specific instructions below -->

### Current Focus Areas
<!-- Example:
- We're migrating from Redux to Zustand - use Zustand for new state management
- Currently refactoring authentication - see docs/adr/003-auth-refactor.md
-->

### Known Issues
<!-- Example:
- CI/CD pipeline times out on Windows - use --maxWorkers=2 for Jest
-->

### Useful Resources
<!-- Add links to:
- API documentation
- Design system
- Database schema
- Architecture diagrams
- Team wiki
-->

---

**Note to Claude Code**: Please follow these instructions for all code changes in this project. When in doubt, prioritize:
1. Writing tests first (ATDD)
2. Keeping changes small and focused
3. Following existing patterns in the codebase
4. Asking for clarification rather than making assumptions
