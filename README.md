# patch-mule-tls-default
Patch för Mule-3.7.0. En bugg i mule (MULE-7663) orsakar att tls-default filen ignoreras och istället används javas default protokoll, i det här fallet Java7 och TLSv1.
Buggen åtgärdades i mule 3.7.4 i klassen RestrictedSSLSocketFactory. Detta projekt bygger och packar RestrictedSSLSocketFactory klassen så den kan användas som en patch för mule 3.7.0.


Se även:
https://www.mulesoft.org/jira/browse/MULE-7663

Note: Buggen gäller klientsidan (outbound-endpoint). Buggen på serversidan åtgärdades i mule 3.7.0.

## Deployment av patch
1. Ladda ned patch från: TODO
2. Lägg skltp-patch-mule-tls-default-VERSION.jar i katalogen: mule-standalone-3.7.0/lib/user

## Testning
Genom exempelvis debug loggning kan man kontrollera att andra protokoll än TLSv1 används, det normala bör vara TLSv1.2. Alternativt kan man stänga av TLSv1 på serversidan och kontrollera att mule kan koppla upp.


