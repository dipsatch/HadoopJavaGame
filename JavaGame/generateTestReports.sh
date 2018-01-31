#!/bin/bash
# Bash script to generate report from each test individually
# Ensure Gradle is correctly installed, and all the below mentioned tests are present in test directory

rm -rf testReports
mkdir testReports

gradle clean
gradle test --tests DummyPlayerTest
gradle jacocoTestReport -Parg=DummyPlayerTest
cp -r build/reports/jacoco/DummyPlayerTest testReports

gradle clean
gradle test --tests NamingTest
gradle jacocoTestReport -Parg=NamingTest
cp -r build/reports/jacoco/NamingTest testReports

gradle clean
gradle test --tests PrintTest
gradle jacocoTestReport -Parg=PrintTest
cp -r build/reports/jacoco/PrintTest testReports

gradle clean
gradle test --tests ProjectileTest
gradle jacocoTestReport -Parg=ProjectileTest
cp -r build/reports/jacoco/ProjectileTest testReports

gradle clean
gradle test --tests VendorTest
gradle jacocoTestReport -Parg=VendorTest
cp -r build/reports/jacoco/VendorTest testReports