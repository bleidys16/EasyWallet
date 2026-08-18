# Plan de Organización de Evidencias - EasyWallet

Este plan detalla la reestructuración del proceso de captura de evidencias para cumplir con los requisitos académicos del ADSO (SENA), incluyendo retardos controlados para capturar mensajes de éxito y la organización de archivos en carpetas descriptivas.

## Cambios en el Código

Para permitir que los mensajes (Toasts) sean visibles el tiempo suficiente para las capturas, se realizarán las siguientes modificaciones:

### [MODIFY] [RegistroActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/RegistroActivity.kt)
- Agregar un retardo de 3 segundos antes de ejecutar `finish()` tras un registro exitoso.

### [MODIFY] [LoginActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/LoginActivity.kt)
- Agregar un retardo de 3 segundos antes de navegar al `HomeActivity` tras un inicio de sesión exitoso.

## Organización de Capturas

Se creará una carpeta dedicada a las evidencias y se guardarán las capturas con los nombres exactos solicitados:

### Carpetas y Archivos
- **Ruta**: [evidencias](file:///C:/Users/Aprendiz/Downloads/EasyWallet/evidencias/)
- **Archivos**:
  1. `captura_login.png`
  2. `captura_registro.png`
  3. `captura_registro_exitoso.png` (Con el mensaje "Usuario registrado correctamente")
  4. `captura_login_exitoso.png` (Con el mensaje de bienvenida)
  5. `captura_login_incorrecto.png` (Con el mensaje de error)
  6. `captura_home.png`
  7. `captura_database_inspector_db.png`
  8. `captura_database_inspector_usuarios.png`

## Plan de Verificación

### Manual
- Ejecutar la aplicación en el emulador.
- Realizar el flujo de Registro y Login.
- Verificar que las imágenes se guarden correctamente en la carpeta de evidencias.

### Automatizado
- Usar comandos `adb` para capturar la pantalla en los momentos exactos del flujo.
