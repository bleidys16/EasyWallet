# Análisis de Estado del Proyecto: Billetera Virtual (ADSO - SENA)

He realizado una revisión exhaustiva del código fuente actual frente a los requisitos de la actividad académica. A continuación se detalla qué se ha completado y qué está pendiente o requiere atención.

## 1. Resumen de Cumplimiento de Requisitos

| Requisito | Estado | Archivo / Ubicación |
| :--- | :--- | :--- |
| **Pantalla Splash** | ✅ Hecho | [SplashActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/SplashActivity.kt) |
| **Pantalla Login** | ✅ Hecho | [LoginActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/LoginActivity.kt) |
| **Registro de Usuario** | ✅ Hecho | [RegistroActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/RegistroActivity.kt) |
| **Persistencia con Room** | ✅ Hecho | [database/](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/) |
| **DAO de Usuario** | ✅ Hecho | [UsuarioDao.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/UsuarioDao.kt) |
| **Singleton AppDatabase** | ✅ Hecho | [AppDatabase.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/database/AppDatabase.kt) |
| **View Binding** | ✅ Hecho | `build.gradle.kts` y en todas las Activities. |
| **Uso de Coroutines** | ✅ Hecho | Uso de `lifecycleScope.launch` en todas las operaciones de DB. |
| **Validación de Login** | ✅ Hecho | Lógica implementada en `LoginActivity`. |
| **Evitar correos duplicados** | ✅ Hecho | Lógica implementada en `RegistroActivity`. |
| **Pantalla Home** | ✅ Hecho | [HomeActivity.kt](file:///C:/Users/Aprendiz/Downloads/EasyWallet/app/src/main/java/com/example/easywallet/HomeActivity.kt) |
| **Reto Adicional (Saldo/Transacciones)** | 🚀 Avanzado | Implementado en `HistoryActivity`, `TransferActivity`, etc. |

## 2. Detalle Técnico de lo Implementado

### Arquitectura (Activity → DAO → Room → SQLite)
El flujo solicitado se cumple estrictamente. Las Activities no ejecutan SQL directo; llaman a métodos suspendidos del DAO dentro de un bloque `lifecycleScope.launch`.

### Base de Datos (`billetera.db`)
- **Entidad Usuario**: Cumple con los campos `id` (PK autoincremental), `nombre`, `correo` y `password`.
- **Entidad Transaccion**: Ya implementada para el manejo de saldos, superando la fase inicial solicitada.

### Interfaz y UX (Material Design)
- Uso de `TextInputLayout` y `TextInputEditText` para entradas de texto.
- Implementación de visibilidad de contraseña (mostrar/ocultar).
- Splash con animación de entrada (Fade In).

## 3. Lo que "NO ESTÁ HECHO" (O requiere ajustes menores)

Aunque el proyecto está muy avanzado, hay puntos de la guía que deben verificarse manualmente o que se pueden pulir:

1.  **Validaciones Visuales vs Toasts**:
    - La guía sugiere "Mostrar mensajes de error". Actualmente se usan muchos `Toast`. Para una mejor experiencia Material Design, se podría usar `binding.tilCorreo.error = "..."`.
2.  **Pruebas Manuales (Sección 14)**:
    - Es necesario ejecutar la aplicación en el emulador (Pixel 7 API 33+) para confirmar visualmente que los mensajes específicos como *"El correo ya está registrado"* aparecen exactamente cuando deben.
3.  **Evidencias (Sección 16)**:
    - Estas capturas de pantalla deben ser tomadas por el estudiante durante la ejecución del emulador. El código para generar la base de datos y las tablas ya está listo.
4.  **Separación de Fases**:
    - El proyecto ya incluye el "Reto Adicional" (Transacciones, saldo variable, transferencias). Si el profesor pide **estrictamente** completar primero el Login/Registro antes de ver el Saldo, el código de `HomeActivity` ya tiene lógica de saldo que podría considerarse "adelantada".

## 4. Próximos Pasos Recomendados

> [!TIP]
> Dado que el código ya está completo incluso con funcionalidades avanzadas, mi recomendación es proceder directamente a la **Fase 10: Realizar todas las pruebas** y **Fase 11: Revisar Database Inspector**.

> [!IMPORTANT]
> He corregido recientemente el `WalletRepository` para asegurar que las pruebas unitarias pasen y la lógica de negocio esté centralizada. Esto garantiza que la aplicación sea estable para las demostraciones.
