Plume Test
----------
Provide common Plume dependencies for tests and integration tests as well as the `MockedClock` class to ease mocking the `Clock` object.

Included dependencies:
- Basic JUnit framework: [JUnit 5](https://junit.org/junit5/)
- Test assertions done in a fluent and more readable way: [Assertj](https://github.com/assertj/assertj)
- To create quick mocks: [Mockito](https://github.com/mockito/mockito)
- Integration tests with Guice: [Guice-junit](https://github.com/Coreoz/Guice-Junit)

Sample usage of `MockedClock` with Guice to create a test module that overrides the default `Clock`: 
```java
public class TestModule extends AbstractModule {
	@Override
	protected void configure() {
		install(Modules.override(new ApplicationModule()).with(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Clock.class).to(MockedClock.class);
			}
		}));
	}
}
```

This module can then be used with the `@GuiceTest` annotation. See [Guice JUnit documentation](https://github.com/Coreoz/Guice-Junit#getting-started) for details.
