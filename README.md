Here is a professional `README.md` you can add to your GitHub repository.

***

# Insurance Premium Calculator

## Overview

This is a simple Java console-based application that calculates an insurance premium based on:

* Customer age
* Vehicle type
* Number of previous insurance claims

The program starts with a base premium amount and applies additional charges according to predefined business rules.

***

## Objective

The purpose of this application is to demonstrate how insurance companies can adjust premium amounts based on customer risk factors.

The premium calculation considers:

1. Age-based risk loading
2. Vehicle-type risk loading
3. Claim history loading

***

## Business Rules

### Base Premium

The application starts with a fixed base premium:

```java
BASE_PREMIUM = 3000
```

### Claim Loading

Each previous insurance claim increases the premium by:

```java
CLAIM_LOADING_PER_CLAIM = 500
```

***

## Premium Calculation Logic

### 1. Age-Based Loading

Additional premium is added based on the customer's age.

| Age Group               | Loading Applied       |
| ----------------------- | --------------------- |
| Below 25 years          | 20% of base premium   |
| Above 60 years          | 10% of base premium   |
| Between 25 and 60 years | No additional loading |

Example:

```text
Base Premium = 3000
Age = 22

Additional Loading = 20% of 3000 = 600
```

***

### 2. Vehicle Type Loading

Additional premium is added based on vehicle type.

| Vehicle Type | Loading Applied       |
| ------------ | --------------------- |
| SUV          | 25% of base premium   |
| SEDAN        | 15% of base premium   |
| HATCHBACK    | No additional loading |

Example:

```text
Base Premium = 3000
Vehicle = SUV

Additional Loading = 25% of 3000 = 750
```

***

### 3. Previous Claim Loading

The premium increases by a fixed amount for every claim previously made.

Formula:

```text
Claim Loading = Number of Claims × 500
```

Example:

```text
Claims = 3

Claim Loading = 3 × 500 = 1500
```

***

## Formula Used

```text
Final Premium =
Base Premium
+ Age Loading
+ Vehicle Loading
+ Claim Loading
```

***

## Sample Calculation

### Input

```text
Age: 22
Vehicle Type: SUV
Number of Claims: 2
```

### Calculation

```text
Base Premium = 3000

Age Loading (20%) = 600

Vehicle Loading (25%) = 750

Claim Loading = 2 × 500 = 1000
```

### Final Premium

```text
Final Premium = 3000 + 600 + 750 + 1000

Final Premium = 5350
```

***

## Technologies Used

* Java
* Scanner Class (for user input)
* Conditional Statements (`if-else`)
* Methods
* Constants (`static final`)

***

## Program Structure

### Constants

```java
BASE_PREMIUM
CLAIM_LOADING_PER_CLAIM
```

These constants ensure that premium values remain fixed throughout the application.

### Main Method

The `main()` method:

1. Accepts user input.
2. Calls the premium calculation method.
3. Displays the final insurance premium.

### calculatePremium() Method

The `calculatePremium()` method performs all premium calculations and returns the final premium amount.

```java
public static int calculatePremium(
    int age,
    String vehicleType,
    int numberOfClaims
)
```

***

## Learning Concepts Demonstrated

* Java Variables and Constants
* User Input Handling
* Method Creation and Invocation
* Business Logic Implementation
* Conditional Decision Making
* Percentage-Based Calculations

***

## Assignment 9: JUnit 5 Unit Tests

All tests live under `com.wipfli.insurance.policy/test` and use the fixed six-policy dataset and `REFERENCE_DATE` from Assignment 8. No test calls `LocalDate.now()`.

### Test List for `StandardPremiumCalculator.calculatePremium(PolicyDetail)`

1. `carPolicy_youngDriverWithOneClaim_returnsBasePlusSurchargeAndClaims`
2. `bikePolicy_experiencedDriverWithTwoClaims_returnsBasePlusClaimsOnly`
3. `truckPolicy_youngDriverWithNoClaims_returnsBasePlusSurcharge`
4. `carPolicy_experiencedDriverWithZeroClaims_returnsBaseOnly`
5. `bikePolicy_youngDriverWithZeroClaims_returnsBasePlusSurcharge`
6. `carPolicy_multipleClaims_addsPerClaimSurcharge`

### Test Classes

| Class under test | Test class |
| ---------------- | ---------- |
| `service.StandardPremiumCalculator` | `service.StandardPremiumCalculatorTest` |
| `service.PolicyValidator` | `service.PolicyValidatorTest` |
| `service.NoClaimBonusCalculator` | `service.NoClaimBonusCalculatorTest` |
| `service.PolicyRegister` | `service.PolicyRegisterTest` |
| Exception hierarchy | `service.ExceptionHierarchyTest` |

### Running the Tests

From the project root:

```powershell
$java = "C:\Users\MishraG\Downloads\openjdk-17.0.2_windows-x64_bin\jdk-17.0.2\bin\java.exe"
$cp = "out\production\premium;out\test\premium;lib\junit-platform-console-standalone-1.11.4.jar"
& $java -cp $cp org.junit.platform.console.ConsoleLauncher --select-package service"
```

### Test Results

```text
31 tests found
31 tests started
31 tests successful
0 tests failed
```
