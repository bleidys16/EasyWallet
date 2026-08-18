# Entrega Final de Evidencias - Billetera Virtual (ADSO - SENA)

Se han completado todas las fases de desarrollo y pruebas solicitadas en la guía de aprendizaje. A continuación se presenta el resumen de las evidencias generadas, organizadas en la carpeta de [evidencias](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/).

## 1. Capturas de Pantalla (Evidencias de UI)

Se han capturado los momentos clave del flujo de la aplicación siguiendo estrictamente la Sección 14 y 16 de la guía:

````carousel
![Pantalla de Login Inicial](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_login.png)
Captura 1: Pantalla de inicio de sesión.
<!-- slide -->
![Pantalla de Registro](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_registro.png)
Captura 2: Formulario de registro de nuevo usuario.
<!-- slide -->
![Registro Exitoso](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_registro_exitoso.png)
Captura 3: Mensaje "Usuario registrado correctamente".
<!-- slide -->
![Login Incorrecto](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_login_incorrecto.png)
Captura 4: Mensaje "Correo o contraseña incorrectos".
<!-- slide -->
![Login Exitoso](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_login_exitoso.png)
Captura 5: Mensaje de bienvenida tras login correcto.
<!-- slide -->
![Pantalla Home](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_home.png)
Captura 6: Interfaz principal (Home) con saldo disponible.
````

### Pruebas Adicionales de Validación
- **Registro Duplicado**: [captura_registro_duplicado.png](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_registro_duplicado.png) (Mensaje: "El correo ya está registrado").
- **Contraseñas Diferentes**: [captura_registro_error_password.png](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_registro_error_password.png) (Mensaje: "Las contraseñas no coinciden").
- **Campos Vacíos**: [captura_registro_campos_vacios.png](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/captura_registro_campos_vacios.png) (Mensaje: "Por favor complete todos los campos").

---

## 2. Evidencias de Persistencia (SQLite / Room)

Se ha extraído el archivo de base de datos generado por Room para su inspección:
- **Base de Datos**: [billetera.db](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/billetera.db)

### Código Fuente de Infraestructura
Los siguientes archivos definen la arquitectura Room solicitada:
- [Usuario.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/Usuario.kt) (Entity)
- [UsuarioDao.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/UsuarioDao.kt) (DAO)
- [AppDatabase.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/AppDatabase.kt) (RoomDatabase Singleton)

---

## 3. Conclusión de la Actividad

> [!IMPORTANT]
> La aplicación cumple con el flujo **Splash → Login → Registro → Room/SQLite → Home**.
> Se han implementado las Coroutines para todas las operaciones de base de datos y se ha utilizado View Binding de manera global.

> [!TIP]
> Para ver los datos en tiempo real, puedes usar el **Database Inspector** de Android Studio mientras el emulador está en ejecución, seleccionando el proceso `com.example.easywallet`.

El proyecto está listo para la entrega académica.
