/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

/**
 * I'm starting a coding exercise to become familiar with the net4j communications framework in general and to learn how
 * to develop a custom net4j transport in particular.
 * <p>
 * 
 * I'll start by implementing my own version of a JVM protocol similar to the one that ships with netj4.
 * <p>
 * 
 * But first, since I'm extending existing net4j frameworks, I want to begin by exploring fundamental net4j framework
 * concepts. I'm using unit tests to do this. The idea is to use the JUnit testing framework as a way to create a
 * collection of executable code samples. So the point of my unit tests is not to "test" net4j for correct behavior, but
 * to demonstrate net4j's behavior in small, easy to digest chunks that just happen to be in the form of unit tests.
 * <p>
 * 
 * I'll start with net4j's "Operations & Maintenance" (OM) framework, as this framework includes both foundational
 * (e.g., bundle) and cross-cutting (e.g., logging and tracking) abstractions. The OM framework unit tests are
 * documented here: {@link org.code.trek.net4j.tests.OmPlatformTest OmPlatformTest}
 */
package org.code.trek.net4j.tests;