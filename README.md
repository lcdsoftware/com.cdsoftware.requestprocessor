# com.cdsoftware.requestprocessor

- Copyright: 2024 cdsoftware
- Repository: <https://github.com/alaracds/com.cdsoftware.requestprocessor>
- License: GPL 2

## Description

Plugin for iDempiere 13 that customizes automatic Request Processor email notifications without modifying core.

The plugin replaces the standard `RequestProcessor` through an OSGi `IServerFactory` with higher service ranking and supports configurable `R_MailText` templates per request event.

## Contributors

- Put the contributor list here, format: Year Name <name@email.com>.

## Components

- iDempiere Plugin [com.cdsoftware.requestprocessor](com.cdsoftware.requestprocessor)
- iDempiere Unit Test Fragment [com.cdsoftware.requestprocessor.test](com.cdsoftware.requestprocessor.test)

## Prerequisites

- Java 17, commands `java` and `javac`.
- iDempiere 13.0.0
- Set `IDEMPIERE_REPOSITORY` env variable

## Features/Documentation

- Custom `RequestProcessor` implementation registered from the plugin.
- Email templates resolved from `CDS_RequestMailTemplate`.
- Supported request events:
  - `RequestDue`
  - `RequestAlert`
  - `RequestInactive`
  - `RequestEscalate`
- Native iDempiere email behavior is preserved as fallback when no template is configured.
- HTML `R_MailText` templates are supported.
- The `@Summary@` variable can render HTML stored in `R_Request.Summary`.
- PDF attachment generation remains compatible with the standard processor.

## Instructions

1. Install the plugin in iDempiere 13.
2. Create the `CDS_RequestMailTemplate` dictionary/table metadata.
3. Create one or more `R_MailText` records.
4. Create `CDS_RequestMailTemplate` records for each event that should use a custom template.
5. Restart iDempiere or reload the Server Manager.

## Extra Links

- Main plugin documentation: [com.cdsoftware.requestprocessor/README.md](com.cdsoftware.requestprocessor/README.md)

## Compile Plugin

A target platform is necessary to **compile** an iDempiere plugin.

For more information about how to build a plugin go to [https://github.com/ingeint/idempiere-target-platform-plugin](https://github.com/ingeint/idempiere-target-platform-plugin)
