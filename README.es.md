# Priveé MVP

## About

El problema detectado que se desea resolver es la pérdida de tiempo de todas las personas involucradas en el proceso de entrada de una privada. como pueden ser los socios/residentes, invitados de los mismos, personal de caseta y proveedores de servicios como luz, agua, internet, basura, es decir, existe una pésima gestión en el proceso de entrada y salida de personas que no son los residentes del recinto.

El problema radica en que mientras los residentes tienen en la mayoría de las privadas un proceso bien definido y ágil para entrar y salir de la privada, los invitados y proveedores de servicio no, causando congestionamiento y filas de minutos, causando retrasos en todas las personas mencionadas anteriormente debido a que el personal de caseta debe verificar la identidad de la persona que desea entrar y verificar que realmente es esperado en la privada, así como tomar algunos datos personales en caso de que llegara a pasar alguna circunstancia o accidente en la que fueran responsables.

A veces se tienen procesos no muy bien definidos en las privadas para el acceso de servicios o de invitados que le quitan tiempo a los residentes ya que tienen que llamar a las casetas en algunos casos para avisar pero a veces no es un proceso ágil por lo mismo que los encargados de las casetas pueden estar atendiendo la entrada de invitados o proveedores de servicios.

Este proyecto busca poner una solución a esta problemática social y, además de mejorar la gestión de la privada reduciendo los tiempos de espera, también busca reducir la huella de carbono con la reducción de embotellamiento de automóviles.

## Funcionalidades principales

### Inicio de sesión.

Los residentes, gatekeepers y administradores que hagan uso de la aplicación tendrán que autenticarse con un correo y una contraseña.

### Gestión de información de residentes.

El sistema permitirá el registro de los residentes de la privada, con el fin de consultarla posteriormente. Se registrará el nombre completo del residente, su fecha de nacimiento, correo electrónico y número de teléfono. Únicamente los administradores puedes obtener la lista de residentes registrados y pueden dar de baja a un residente.

### Gestión de la información de los porteros.

El sistema permitirá el registro de un portero de la privada. Se registrará el nombre del portero, su correo electrónico y número de teléfono. Únicamente un administrador podrá listar a los gatekeepers registrados y dar de baja a un portero.

### Gestión de la información de los administradores.

Únicamente un administrador será capaz de crear a otro usuario con privilegios de administrador. Se registrará el nombre, teléfono, correo electrónico y la contraseña del nuevo administrador.

### Generar pases para invitados.

La función principal del sistema. Los residentes podrán generar pases para permitir acceso a un invitado a su residencia.

### Gestión de pases.

El usuario será capaz de editar la información de un pase o invalidar el pase para que ya no pueda ser usado.

### Historial de pases.

El usuario podrá consultar el historial de todos los pases que ha generado. De igual manera, los administradores pueden consultar el historial de los pases generados por un residente.

### Verificación de pases.

El sistema se encarga de analizar los pases de los invitados para verificar su autenticidad y cumplan con los criterios de seguridad establecidos. Únicamente un gatekeeper tiene los permisos para validar los pases.

## Project structure

- **me.hikingcarrot7.privee.repositories**: Esta capa se encarga de la comunicación con la base de datos. En este proyecto, se usó H2 como base de datos en memoria.

- **me.hikingcarrot7.privee.services**: Esta capa se encarga de la lógica de negocio y se comunica con la capa `repository` para el acceso a las entidades.

- **me.hikingcarrot7.moodleripoff.web**: Esta capa expone los servicios por medio de una REST(less) API, y se comunica con la capa `service`. Esta capa también se encarga de validar los datos de entrada con el uso de la especificación **jakarta.validation**. De igual forma, esta capa se encarga de la documentación de la API, el manejo de errores y la autenticación y autorización de los usuarios.

- **me.hikingcarrot7.moodleripoff.models**: Aquí se encuentrar definidas las entidades del sistema y sus relaciones. In a nutshell, de está forma interactuan las entidades:

  - Todo el sistema gira entorno a `Invitations`. Un `Resident` puede tener 0 a muchas invitations.
  - Únicamente un residente puede crear un invitations. Cuando un invitation es creada, se genera un código QR que contiene un token asociado con la invitación. Además, se lanza un evento que tiene como objetivo notificarle al residente por email que la invitation ha sido creada.
  - Cada una de estas invitations únicamente pueden ser validadas por un `Gatekeeper`.
  - Cuando un invitation es validada, se crea un registro de tipo `InvitationVerification` que contiene la fecha en la que se verificó y el gatekeeper que lo verificó.
  - Historial de los invitations puede ser consultado por el residente o un `Admin`.

- **me.hikingcarrot7.moodleripoff.scheduling**: Aquí se encuentra toda la funcionalidad relacionado con la revisión programada de la validez de las invitations pendientes. Cada 30 segundos, el sistema revisa si hay invitations que ya hayan expirado y las marca como expiradas. Seguido de esto, se lanza una evento para notificarle al usuario para realizar las acciones que considere necesarias.

- **me.hikingcarrot7.moodleripoff.events**: Aquí se encentrarn todas las clases relacionadas con eventos:

  - _*InvitationCreationEvent.java*_: Se lanza cuando se ha creado una invitación por un residente.

  - _*InvitationExpiredEvent.java*_: Se lanza cuando ha expirado la vigencia de una invitation.

## Deploy to Payara Cloud

El proyecto se encuentra hecho deploy en el siguiente URL (PAYARA_HOST): [https://start-dev-2601fe95.payara.app/privee/](https://start-dev-2601fe95.payara.app/privee/)

> Default ADMIN credentials:

```json
{
  "email": "admin@gmail.com",
  "password": "123456789"
}
```

> Default ADMIN URL {{PAYARA_HOST}}/api/admins/434bd194-6111-43d0-b5fe-5a2c84d68ae1

## Módulos

Todos los endpoints se encuentran documentados utilizando la especificación `Microprofile OpenAPI`. Toda la documentación se puede encontrar el la ruta: `{{PAYARA_HOST}}/api/openapi-ui/index.html`

> Por problemas descritos con detenimiento en el apartado [Posiblemas mejoras y problemas](#posibles-mejoras-y-problemas). No es posible acceder a esta ruta en producción.

Las rutas existen dentro de `Postman`, exportadas en el archivo `./postman_api_collection.json`

### Login

Se determinó que la seguridad para este proyecto es muy importante, para ello se ha decido proteger la API por medio de `Microprofile JWT`, por lo que es necesario autenticarse para poder consumir los servicios.

Por cuestiones de simplicidad, se decidió que los residentes, gatekeepers y administradores tendrían sus propios endpoints para el login. Esto con el objetivo de simplificar la validación de los datos y la estructura de las entidades.

- Para el login de los **Residents** se puede hacer uso del endpoint `/api/login/resident`.
- Para el login de los **Getekeepers** se puede hacer uso del endpoint `/api/login/gatekeeper`.
- Para el login de los **Admins** se puede hacer uso del endpoint `/api/login/admin`.

Únicamente los administradores pueden registrar residentes, gatekeepers y a otros administradores.

La implementación de la autenticación y autorización se realizó con el uso de la especificación **Microprofile JWT**. La lógica para la generación de los tokens se hace en la clase `me.hikingcarrot7.privee.web.security.JWTTokenGenerator` utilizando la llave privada que se encuentra en el archivo `src/main/resources/META-INF/microprofile-config.properties`. Para la generación de la llave privada se utilizó [jwtenizr](https://github.com/AdamBien/jwtenizr).

### Residentes

Las siguiente funcionalidades se implementarion para los residentes

- Listado de los residentes. Este endpoint retorna la lista de los residentes registrados en el sistema. Solamente puede ser usado por administradores.

  > GET {{PAYARA_HOST}}/api/residents

- Obtener residente por ID. Puede ser utilizado por residentes y administradores.

  > GET {{PAYARA_HOST}}/api/residents/:residentId

- Obtener al residente que tiene la sesión iniciada.

  > GET {{PAYARA_HOST}}/api/residents/me

- Crear un residente. Solamente puede ser utilizado por un administrador. Los campos que se necesitan son: `firstName`, `lastName`, `phone`, `email` y `password`.

  > POST {{PAYARA_HOST}}/api/residents

- Actualizar un residente. Los campos que se puede actualizar son: `firstName`, `lastName` y `phone`.

  > PUT {{PAYARA_HOST}}/api/residents/:residentId

- Eliminar un residente. Esta functiona hace un `soft delete` de la entidad y no la elimina por completo de la base de datos. Únicamente un admin puede hacer uso de esta funcionalidad.

  > DELETE {{PAYARA_HOST}}/api/residents/:residentId

### Gatekeepers

Las siguiente funcionalidades se implementarion para los gatekeepers

- Listado de los gatekeepers. Este endpoint retorna la lista de los gatekeepers registrados en el sistema. Solamente puede ser usado por administradores.

  > GET {{PAYARA_HOST}}/api/gatekeepers

- Obtener gatekeeper por ID. Puede ser utilizado por gatekeepers y administradores.

  > GET {{PAYARA_HOST}}/api/gatekeepers/:gatekeeperId

- Obtener al gatekeeper que tiene la sesión iniciada.

  > GET {{PAYARA_HOST}}/api/gatekeepers/me

- Crear un gatekeeper. Solamente puede ser utilizado por un administrador. Los campos que se necesitan son: `firstName`, `lastName`, `phone`, `email` y `password`.

  > POST {{PAYARA_HOST}}/api/gatekeepers

- Actualizar un gatekeeper. Los campos que se puede actualizar son: `firstName`, `lastName` y `phone`.

  > PUT {{PAYARA_HOST}}/api/gatekeepers/:gatekeeperId

- Eliminar un gatekeeper. Esta functiona hace un `soft delete` de la entidad y no la elimina por completo de la base de datos. Únicamente un admin puede hacer uso de esta funcionalidad.

  > DELETE {{PAYARA_HOST}}/api/gatekeepers/:gatekeeperId

### Administradores

Las siguiente funcionalidades se implementarion para los admins

- Listado de los admins. Este endpoint retorna la lista de los admins registrados en el sistema. Solamente puede ser usado por administradores.

  > GET {{PAYARA_HOST}}/api/admins

- Obtener admin por ID. Puede ser utilizado por administradores.

  > GET {{PAYARA_HOST}}/api/admins/:adminId

- Obtener al admin que tiene la sesión iniciada.

  > GET {{PAYARA_HOST}}/api/admins/me

- Crear un admin. Solamente puede ser utilizado por un administrador. Los campos que se necesitan son: `firstName`, `lastName`, `phone`, `email` y `password`.

  > POST {{PAYARA_HOST}}/api/admins

- Actualizar un admin. Los campos que se puede actualizar son: `firstName`, `lastName` y `phone`.

  > PUT {{PAYARA_HOST}}/api/admins/:adminId

- Eliminar un admin. Esta functiona hace un `soft delete` de la entidad y no la elimina por completo de la base de datos. Únicamente un admin puede hacer uso de esta funcionalidad.

  > DELETE {{PAYARA_HOST}}/api/admins/:adminId

### Invitaciones

Las siguiente funcionalidades se implementarion para las invitaciones

- Obtener historial de las invitaciones en formato JSON. Este endpoint puede ser utilizando por residentes y administradores.

  > GET {{PAYARA_HOST}}/api/invitations?residentId=:residentId

Se debe pasar como query param el ID del residente

- Obtener historial de las invitaciones en formato Excel. Utilizando utilizando las capacidades de `Content negociation` de la especificación de `Jakarta RESTful Web Services`, es posible solicitar el historial de invitaciones de un residente en formato Excel. Para ello es posible especificar el header `Accept: application/vnd.ms-excel`.

  > GET {{PAYARA_HOST}}/api/invitations?residentId=:residentId

  > Por problemas descritos con detenimiento en el apartado [Posiblemas mejoras y problemas](#posibles-mejoras-y-problemas). No es posible acceder a esta ruta en producción.

- Obtener una invitación por ID. Este endpoint solo puede ser usar por residentes

  > GET {{PAYARA_HOST}}/api/invitations/:invitationId

- Crear una invitación. Este endpoint solo se puede utilizar por un residente. Los campos que se necesitan son: `expirationDate`, `guestName`, `guestEmail` y `vehiclePlane`.

  > POST {{PAYARA_HOST}}/api/invitations/:invitationId

- Validar un invitación. Este endpoint únicamente puede ser utilizando por gatekeepers. Cuando una invitación se valida, pasa a estado `VALIDATED`.

  > POST {{PAYARA_HOST}}/api/invitations/validate/:token?gatekeeperId=:gatekeeperId

- Cancelar invitación. Este endpoint solo puede ser utilizando por residentes.

  > DELETE {{PAYARA_HOST}}/api/invitations/:invitationId

## Posibles mejoras y problemas

- Envío de correos cuando una invitación se crea o expira. Dado a limitaciones de conocimiento, no fue posible implementar está funcionalidad en esta primera versión. Este tipo de notificaciones se considera importante para este proyecto para evitar cualquier retraso con la entrada. Se planeaba utilizar la especificación `Jakarta Mail` para implementar esta funcionalidad.

- Un funcionalidad que podría ser muy útil es que un residente pueda 'asignar' a un residente (familiar), esto con el fin de tener mayor flexibilidad a la hora de crear invitaciones y que no todo dependa de una sola persona.

- Tener integración con `Github Actions` para realizar deployments automáticas.

- Utilizar la especificación `Microprofile Metrics` en lugar de utilizar la anotación `PerformanceLog`. Esto con el fin de tener un sistema de monitoreo más robusto del sistema y encontrar posibles problema de redimiento lo antes posible.

- Problemas con la documentación de `Miroprofile OpenAPI`. Al especificar un `contextRoot` al hacer el deploy en `Payara Cloud`, ya no es posible acceder a ninguna ruta de documentación con OpenAPI (`{{PAYARA_HOST}}/privee/openapi`). Una solución que se intentó fue crear un 'Proxy' cómo se puede observar en la clase **src/main/java/me/hikingcarrot7/privee/web/controllers/OpenApiProxyRestFacade.java** pero por cuestiones de tiempo no se logró llegar a una solución adecuada.

- Hay un problema al momento de generar el documento excel en `Payara Cloud`. Lamentablemente, la librería que se utilizó para generar los reportes tiene dependencias con unas clases de **java.awt.** que no están disponibles en el JDK (JDK 17) que está disponible para configurar desde Payara Cloud.
