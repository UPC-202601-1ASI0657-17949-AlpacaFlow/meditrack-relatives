# 5.3.1.3 Testing Suite Evidence for Sprint Review

## Microservicio Relatives

Validar el núcleo del backend del microservicio relatives en cuatro dimensiones:
- Arranque de aplicación.
- Reglas de dominio y validaciones.
- Servicios de aplicación (CQRS ligero con Mockito para comandos y consultas).
- Endpoints REST mediante pruebas integrales.

### Tecnologías Usadas:
- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- H2 Database (perfil test)
- Spring Security Test

---

### Cobertura implementada:

#### RelativesApplicationTests

| Test | Validación | Valor |
|------|-----------|-------|
| `contextLoads()` | El contexto de Spring Boot inicia sin errores de wiring/configuración | Prueba de sanidad de infraestructura |

---

#### RelativeCommandServiceImplTest

| Test | Validación |
|------|-----------|
| `shouldCreateRelativeSuccessfully` | Valida que un familiar se registre correctamente retornando su ID |
| `shouldThrowWhenCreateFailsWithException` | Verifica manejo de errores en persistencia |
| `shouldUpdateRelativeSuccessfully` | Verifica la actualización correcta de datos del familiar |
| `shouldThrowWhenRelativeNotFoundOnUpdate` | Valida excepción al actualizar un familiar inexistente |
| `shouldDeleteRelativeSuccessfully` | Verifica la eliminación de un familiar existente |
| `shouldThrowWhenRelativeNotFoundOnDelete` | Valida excepción al eliminar un familiar inexistente |
| `shouldAssignRelativeToSeniorCitizenSuccessfully` | Verifica la asignación exitosa de un familiar a un adulto mayor |
| `shouldThrowWhenAssigningToNonExistentRelative` | Valida excepción al asignar a un familiar inexistente |
| `shouldUnassignRelativeFromSeniorCitizenSuccessfully` | Verifica la desasignación de un familiar de un adulto mayor |
| `shouldThrowWhenUnassigningFromNonExistentRelative` | Valida excepción al desasignar de un familiar inexistente |

**Valor:** Asegura el funcionamiento correcto de las reglas de negocio del ciclo de vida de familiares y validación de errores.

---

#### RelativeQueryServiceImplTest

| Test | Validación |
|------|-----------|
| `shouldReturnAllRelatives` | Valida la obtención de todos los familiares registrados |
| `shouldReturnEmptyListWhenNoRelatives` | Verifica respuesta vacía cuando no hay familiares |
| `shouldReturnRelativeById` | Verifica la búsqueda por ID de un familiar existente |
| `shouldReturnEmptyWhenRelativeNotFoundById` | Verifica retorno vacío para ID inexistente |
| `shouldReturnRelativeByUserId` | Verifica la búsqueda por ID de usuario asociado |
| `shouldReturnEmptyWhenNoRelativeForUserId` | Verifica retorno vacío para usuario sin familiar |
| `shouldReturnRelativesBySeniorCitizenId` | Valida la obtención de familiares asignados a un adulto mayor |
| `shouldReturnEmptyListWhenNoRelativesForSeniorCitizen` | Verifica retorno vacío para adulto mayor sin familiares |

**Valor:** Valida comportamiento de lectura y separación de responsabilidades CQRS.

---

#### RelativesControllerIntegrationTest

| Test | Validación |
|------|-----------|
| `shouldCreateRelativeSuccessfully` | Prueba integral que valida el flujo completo POST desde el controlador hasta la base de datos H2 |
| `shouldGetRelativeByIdAfterCreation` | Verifica recuperación GET del familiar creado |
| `shouldReturn404WhenRelativeNotFound` | Verifica respuesta 404 para familiar inexistente |
| `shouldReturnAllRelatives` | Verifica listado completo de familiares |
| `shouldReturn400WhenCreateWithInvalidData` | Valida rechazo de datos inválidos con error BadRequest |
| `shouldGetRelativeByUserIdSuccessfully` | Verifica consulta de familiar por ID de usuario |
| `shouldUpdateRelativeSuccessfully` | Prueba integral del flujo PUT de actualización |
| `shouldReturn404WhenUpdatingNonExistentRelative` | Verifica 404 al actualizar familiar inexistente |
| `shouldDeleteRelativeSuccessfully` | Verifica eliminación completa vía DELETE |
| `shouldReturn404WhenDeletingNonExistentRelative` | Verifica 404 al eliminar familiar inexistente |
| `shouldAssignAndUnassignRelativeToSeniorCitizen` | Prueba integral de asignación y desasignación de familiar |
| `shouldGetRelativesBySeniorCitizenId` | Verifica consulta de familiares por adulto mayor |
| `shouldReturn400WhenAssigningToNonExistentRelative` | Valida 400 al asignar a familiar inexistente |

**Valor:** Asegura el funcionamiento correcto de todo el flujo para llamadas API y persistencia en base de datos.

---

### Documentación API

Adicionalmente, se habilitó la documentación interactiva de la API mediante **Springdoc OpenAPI (Swagger)** disponible en:

- **Swagger UI:** `http://localhost:8083/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8083/v3/api-docs`

Esto permite probar peticiones GET/POST exitosas directamente desde el navegador.
