# Git Commit Core Backend

### Overview 

 The Gift Commit Core backend is a RESTful API written in Kotlin that aims to provide a presistent storage layer 
 to create and manage users and their items. 
 
 Currently, Gift Commit is utilizing Amazon Web Services, deployed on an EC2 instance and writing to a
 MySQL RDS instance.  
 
### Build

#### Requirements 
- Maven
- Docker 


Gift Commit uses Maven to compile, test, and build a Docker container. 

```
mvn clean package 
```

This will result in a `gift-commit/core` image that can be run by:

```
docker run -d -p 8080:8080 gift-commit/core:latest
```

Interact with the API via http://localhost:8080


### Deploy

Gift Commit is automatically deployed on a GitHub Release. View the [Releases](https://github.com/gift-commit/core/releases)
for a deploy history and changelog for the API.

#### Drafting a Release

To draft a new release, click [Draft a new release](https://github.com/gift-commit/core/releases/new) from the Releases page. 

 1. The `tag version` should be the next version following the [Semver](https://semver.org/) `MAJOR.MINOR.PATCH` notation. 

 2. The `release title` should simply be the version prepended with a `v`. Example: `vX.X.X`
 
 3. The `release description` should contain a log of changes since the last release. Changes should be 
    in the form that [Keep A Changelog](https://keepachangelog.com/en/1.0.0/) documents.


### Secret Management

The repository uses [Travis CI](https://travis-ci.org/) for a CI/CD build system. Travis exposes a [solution to
encrypt secrets](https://docs.travis-ci.com/user/encryption-keys/) via a Travis public key in order to check them into GitHub. 
Travis will decrypt the secrets and make them available during build time. In order to add additional secrets, you will need the `travis` cli. 
To install, `gem install travis`.

To add additional secrets, 

#### Environment Variable 

 1. `travis encrypt SOMEVAR="secretvalue" -add`
 
 This will automatically add an entry in the `.travis.yml` and expose the `SOMEVAR` environment variable with `secretvalue` during the build.
 
#### File 

 1. Add the new secret file locally to `.build/secrets/decrypted/` which should be ignored by `git`.
 2. `gtar -cvf .build/secrets/decrypted/secrets.tar .build/secrets/decrypted/*`
 3. `travis encrypt-file .build/docker/secrets/decrypted/secrets.tar .build/secrets/secrets.tar.enc --add`