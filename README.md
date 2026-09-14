# com.cdsoftware.requestprocessor

- Copyright: 2024 https://www.casadelsoftware.com
- Repository: <https://github.com/alaracds/com.cdsoftware.requestprocessor>
- License: GPL 2

## Description

`com.cdsoftware.requestprocessor` is an iDempiere 13 backend plugin that customizes automatic Request Processor email notifications. It replaces the standard `RequestProcessor` server factory with a higher-ranked OSGi service, resolves configurable `R_MailText` templates per request event, supports HTML email templates, and preserves the native iDempiere email behavior as a fallback.

## Contributors

- 2024 cdsoftware and contributors.
- 2026 Angel Lara <angel@casadelsoftware.com>.

## Components

- iDempiere Plugin [com.cdsoftware.requestprocessor](com.cdsoftware.requestprocessor)
- iDempiere Unit Test Fragment [com.cdsoftware.requestprocessor.test](com.cdsoftware.requestprocessor.test)

## Prerequisites

- Java 17, commands `java` and `javac`.
- iDempiere 13.0.0

## Features/Documentation

### Source Structure

```text
com.cdsoftware.requestprocessor/src
└── com
    └── cdsoftware
        └── requestprocessor
            ├── base
            │   ├── BundleInfo.java
            │   ├── CustomCallout.java
            │   ├── CustomForm.java
            │   └── CustomProcess.java
            ├── component
            │   ├── CalloutFactory.java
            │   ├── EventFactory.java
            │   ├── FormFactory.java
            │   ├── ModelFactory.java
            │   ├── ProcessFactory.java
            │   ├── RequestEventBlacklistConfigurer.java
            │   └── RequestProcessorFactory.java
            ├── event
            │   └── RequestUpdateMailTemplateEvent.java
            ├── model
            │   ├── CDSMailText.java
            │   ├── I_CDS_RequestMailTemplate.java
            │   ├── MRequestMailTemplate.java
            │   └── X_CDS_RequestMailTemplate.java
            ├── server
            │   └── CDSRequestProcessor.java
            └── util
                ├── FileTemplateBuilder.java
                ├── KeyValueLogger.java
                ├── SqlBuilder.java
                └── TimestampUtil.java
```

### OSGi Services

| Component | Service / Purpose | Key Behavior |
| --- | --- | --- |
| `RequestProcessorFactory` | Provides `org.adempiere.server.IServerFactory` for `RequestProcessor` with `service.ranking = 100`. | Creates `CDSRequestProcessor` instances for active `MRequestProcessor` records, allowing this plugin to be selected before the standard factory. |
| `RequestEventBlacklistConfigurer` | Immediate startup component. | Adds `org.adempiere.base.event.RequestEventHandler[idempiere/requestSendEMail]` to `${ADEMPIERE_HOME}/event.handlers.blacklist` when needed to avoid duplicate Request Updated emails. |
| `EventFactory` | Annotation-based event manager for `com.cdsoftware.requestprocessor.event`. | Registers request email event delegates declared in the plugin. |
| `ModelFactory` | Annotation-based model factory for `com.cdsoftware.requestprocessor.model`. | Makes plugin model overrides and generated models available to iDempiere. |
| `CalloutFactory` | Annotation-based callout factory for `com.cdsoftware.requestprocessor.model` and `com.cdsoftware.requestprocessor.callout`. | Provides the extension point for annotated column callouts. |
| `ProcessFactory` | Annotation-based process factory for `com.cdsoftware.requestprocessor.process`. | Provides the extension point for annotated server processes. |
| `FormFactory` | Annotation-based ZK form factory for `com.cdsoftware.requestprocessor.form`. | Provides the extension point for annotated custom forms. |

### Request Processor Email Templates

The plugin stores template routing rules in `CDS_RequestMailTemplate`. Each active record links a request processor event to an `R_MailText` template and may optionally target a specific `R_RequestType_ID`.

| Field | Purpose |
| --- | --- |
| `Value` | Search key for the template routing record. |
| `CDS_EventCode` | Request event handled by the plugin. |
| `R_RequestType_ID` | Optional request type filter. Empty values act as a general fallback for the event. |
| `R_MailText_ID` | Mail template used to render the subject and body. |
| `SeqNo` | Ordering value used after request type, organization, and client matching. |
| `AD_Client_ID`, `AD_Org_ID` | Tenant and organization scope for template selection. |
| `IsActive` | Enables or disables the routing record. |

Template priority is resolved by `MRequestMailTemplate.getFor` in this order:

| Priority | Selection Rule |
| --- | --- |
| 1 | Same `R_RequestType_ID`. |
| 2 | Empty `R_RequestType_ID`. |
| 3 | Same `AD_Org_ID`. |
| 4 | `AD_Org_ID = 0`. |
| 5 | Same `AD_Client_ID`. |
| 6 | `AD_Client_ID = 0`. |
| 7 | `SeqNo`. |
| 8 | `CDS_RequestMailTemplate_ID`. |

If no active `CDS_RequestMailTemplate` record is found, if `R_MailText_ID` is empty, or if the referenced `R_MailText` record does not exist, the plugin falls back to native iDempiere email behavior.

### Supported Request Events

| Event Code | Purpose | Triggering Context |
| --- | --- | --- |
| `RequestDue` | Sends due request notifications with an optional template. | Used when a request reaches its next action date and, through the standard processor path, when a due request transitions into overdue. |
| `RequestAlert` | Sends overdue reminder notifications with an optional template. | Controlled by the Request Processor alert settings such as `OverdueAlertDays` and `RemindDays`. |
| `RequestInactive` | Sends inactivity notifications with an optional template. | Controlled by the Request Processor inactivity settings such as `InactivityAlertDays` and `RemindDays`. |
| `RequestEscalate` | Sends escalation notifications with an optional template. | Used when a request is escalated to the responsible user's supervisor or the processor supervisor. |
| `RequestUpdated` | Sends request update notifications with an optional template. | Used for the standard request send-email event after request changes. |

### Processes

| Class Name | Purpose | Main Parameters | Key Logic & Results |
| --- | --- | --- | --- |
| Not implemented | No process classes are currently present under `com.cdsoftware.requestprocessor.process`. | Not applicable. | `ProcessFactory` is registered as an extension point, but this plugin does not currently provide concrete process classes. |

### Events

| Target Model / Table | Event Timing / Topic | Functional Rule & Objective |
| --- | --- | --- |
| `R_Request` | `idempiere/requestSendEMail` through `RequestUpdateMailTemplateEvent`. | Intercepts request update email delivery, resolves the `RequestUpdated` template, sets request and recipient variables, sends the rendered email, and preserves native notice creation when delivery fails. |

### Callouts

| Callout Class | Target Field / Column | Business Validation & UI Impact |
| --- | --- | --- |
| Not implemented | Not applicable. | `CalloutFactory` is registered as an extension point, but this plugin does not currently provide concrete callout classes. |

### Application Dictionary Metadata (2Pack)

| Package / File Name | Purpose & Dictionary Configurations |
| --- | --- |
| `META-INF/2Pack_1.0.0.zip` | Installs the `CDS_RequestProcessor` dictionary package. It includes the `CDS_RequestMailTemplate` table, `Request Mail Template` window, `Request Mail Template` menu entry, Spanish translations, the `CDS_EventCode` list values, and default `R_MailText` templates for `RequestDue`, `RequestAlert`, `RequestInactive`, `RequestEscalate`, and `RequestUpdated`. |

### Models

| Class | Table / Object | Purpose |
| --- | --- | --- |
| `I_CDS_RequestMailTemplate` | `CDS_RequestMailTemplate` | Generated interface for the request mail template configuration table. |
| `X_CDS_RequestMailTemplate` | `CDS_RequestMailTemplate` | Generated persistent object for the request mail template configuration table. |
| `MRequestMailTemplate` | `CDS_RequestMailTemplate` | Provides supported event constants and selects the best active template for a request and event. |
| `CDSMailText` | `R_MailText` | Extends `MMailText` to support custom variables and HTML rendering for `@Summary@` when the selected mail template is HTML. |

### Mail Template Variables

Templates use standard `R_MailText` variables. Because the plugin sets the request as the parsed PO, request columns can be used directly in template content, for example:

| Variable | Source |
| --- | --- |
| `@DocumentNo@` | `R_Request.DocumentNo` |
| `@Summary@` | `R_Request.Summary`; rendered as HTML when the selected `R_MailText` is marked as HTML. |
| `@R_Status_ID@` | `R_Request.R_Status_ID` |
| `@SalesRep_ID@` | `R_Request.SalesRep_ID` |
| `@DateNextAction@` | `R_Request.DateNextAction` |

`RequestUpdated` templates also receive these custom variables:

| Variable | Purpose |
| --- | --- |
| `@OriginalSubject@` | Native subject generated by iDempiere for the request update. |
| `@RequestUpdateMessage@` | Native plain text update message generated by iDempiere. |
| `@RequestUpdateMessageHtml@` | Native update message converted to HTML; the summary block is rendered as an HTML fragment. |
| `@RecipientName@` | Name of the email recipient. |
| `@SenderName@` | Name of the sender when available. |

### Fallback Behavior

| Event | Native Behavior Preserved |
| --- | --- |
| `RequestDue`, `RequestAlert`, `RequestInactive` | Uses the translated iDempiere message subject, `R_Request.Summary` as the body, the request PDF attachment, and the request responsible user as recipient. |
| `RequestEscalate` | Keeps the standard escalation update, marks the request as escalated, stores the escalation subject in `Result`, and sends to the responsible user and supervisor when applicable. |
| `RequestUpdated` | Sends the native request update message when no `RequestUpdated` template is configured. If sending fails, creates an iDempiere notice for the recipient. |

### Unit Tests

| Test Class | Covered Utility |
| --- | --- |
| `FileTemplateBuilderTest` | Freemarker-backed file template generation helper. |
| `KeyValueLoggerTest` | Key/value logging helper. |
| `SqlBuilderTest` | SQL text builder helper. |
| `TimestampUtilTest` | Timestamp utility behavior. |

## Instructions

1. Install or deploy the `com.cdsoftware.requestprocessor` OSGi bundle in iDempiere 13.
2. Import `META-INF/2Pack_1.0.0.zip` to create the `CDS_RequestMailTemplate` table, window, menu entry, event code list values, translations, and default `R_MailText` records.
3. Create or review the `R_MailText` records that should be used for request notifications.
4. Create active `CDS_RequestMailTemplate` records for each event that should use a custom template.
5. Leave `R_RequestType_ID` empty for a general event template, or set it to a request type when a specific request type needs its own template.
6. Restart iDempiere or reload the Server Manager so the higher-ranked `RequestProcessorFactory` replaces the standard request processor factory.
7. Confirm that `${ADEMPIERE_HOME}/event.handlers.blacklist` contains `org.adempiere.base.event.RequestEventHandler[idempiere/requestSendEMail]` before using `RequestUpdated` templates. The plugin tries to add this entry automatically on startup; if filesystem permissions prevent that update, add it manually and restart iDempiere.

## Extra Links

- Plugin module notes: [com.cdsoftware.requestprocessor/README.md](com.cdsoftware.requestprocessor/README.md)
