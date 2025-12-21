[![CodeQL](https://github.com/paytoncain/TrackFi/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/paytoncain/TrackFi/actions/workflows/github-code-scanning/codeql)
[![Dependabot](https://github.com/paytoncain/TrackFi/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/paytoncain/TrackFi/actions/workflows/dependabot/dependabot-updates)
[![Java CI with Maven](https://github.com/paytoncain/TrackFi/actions/workflows/maven.yml/badge.svg)](https://github.com/paytoncain/TrackFi/actions/workflows/maven.yml)
# TrackFi
## Development
- project:
  - requirements:
    - Maven 3.9+
  - commands:
    - clean previous build: `mvn clean`
    - build artifacts: `mvn package`
    - add artifacts to local maven repository: `mvn install`
    - **All commands can also be applied within modules**
- `trackfi-api`: 
  - requirements:
    - Java 17
    - Docker
  - maven profiles:
    - `embedded`: adds embedded h2 database compatible with `spring-data-jpa` (enabled by default)

### Running TrackFi locally
A docker compose file has been added to the project's root directory to run TrackFi locally. Note that `mvn -Pembedded -Pnative spring-boot:build-image` must be run within the `trackfi-api` directory before attempting to create the stack. This command must be run again for any changes to appear in the resulting docker image.

Commands (from project root):
- Create stack (attached logs): `docker compose up`
- Create stack (detached logs): `docker compose up -d`
- Create stack (detached logs, wait for all containers to report healthy status): `docker compose up -d --wait`
- Destroy stack: `docker compose down`

  