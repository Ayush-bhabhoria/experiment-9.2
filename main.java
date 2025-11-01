name: Java CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      # Step 1: Checkout code
      - name: Checkout repository
        uses: actions/checkout@v4

      # Step 2: Set up JDK
      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'   # You can use 'zulu' or 'corretto' too
          java-version: '17'        # Change to your version if needed

      # Step 3: Cache Maven packages (faster builds)
      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      # Step 4: Build the project with Maven
      - name: Build with Maven
        run: mvn -B clean package --file pom.xml

      # Step 5: Run tests
      - name: Run tests
        run: mvn test

      # Step 6: Upload build artifact (optional)
      - name: Upload build artifact
        uses: actions/upload-artifact@v4
        with:
          name: jar-file
          path: target/*.jar

  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      # Step 7: Deploy step (customize as per your server)
      - name: Deploy Application
        run: |
          echo "Deploying application..."
          # Example (if deploying via SSH):
          # scp target/*.jar user@your-server:/opt/app/
          # ssh user@your-server "sudo systemctl restart myapp"
