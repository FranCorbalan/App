# App "Voces en el Aire" — Compilarla en la nube (sin Android Studio)

Como tu PC es vieja (Windows 8.1, 4GB RAM), NO necesitás instalar nada.
Todo el proyecto ya está armado y listo. Vamos a usar **GitHub Actions**,
un servicio gratuito de Microsoft/GitHub que compila el .apk en sus
servidores. Vos solo subís archivos desde el navegador.

Aviso honesto: armé este proyecto siguiendo la estructura estándar de
Gradle/Android y generé el "wrapper" de Gradle de verdad (no es un
invento), pero no tengo forma de compilarlo acá porque este entorno
no tiene el SDK de Android. Es muy probable que ande, pero si el
workflow de GitHub falla, mandame el log de error (te explico abajo
dónde verlo) y lo arreglamos.

## Paso 1 — Cuenta en GitHub
Si no tenés, creá una gratis en https://github.com

## Paso 2 — Crear el repositorio
1. Botón "+" arriba a la derecha → "New repository"
2. Nombre: `voces-en-el-aire-app` (o el que quieras)
3. Dejalo **Public** (así Actions es gratis sin límite de minutos)
4. NO tildes "Add a README" — dejalo vacío
5. Create repository

## Paso 3 — Editar tus datos ANTES de subir (importante)
En tu PC, abrí (con el Bloc de notas alcanza) el archivo:
`app/src/main/java/com/tintina/vocesenelaire/Constants.kt`
Y completá con tus datos reales: URL del stream, tu email, redes
sociales. Guardá el archivo.
(Si te olvidás, no pasa nada — lo podés editar después directamente
en la web de GitHub, sin volver a subir nada.)

## Paso 4 — Subir los archivos
1. En la página del repo recién creado, vas a ver un link que dice
   "uploading an existing file" — hacé clic ahí.
2. Abrí la carpeta descomprimida `VocesEnElAireApp` en el explorador
   de Windows, seleccioná TODO lo que está DENTRO de esa carpeta
   (los archivos y subcarpetas: `app`, `gradle`, `.github`, `build.gradle.kts`,
   `settings.gradle.kts`, `gradlew`, `gradlew.bat`, `gradle.properties`,
   `.gitignore`, `LEEME_PASO_A_PASO.md`) — OJO: seleccioná el CONTENIDO,
   no la carpeta `VocesEnElAireApp` en sí.
3. Arrastralos todos juntos a la zona de "drag files here" del navegador.
   Chrome y Edge respetan las subcarpetas automáticamente.
4. Esperá que termine de cargar (puede tardar un par de minutos con
   muchos archivos) y hacé clic en "Commit changes" abajo de todo.

## Paso 5 — Ver la compilación automática
1. Andá a la pestaña **Actions** del repositorio (arriba).
2. Vas a ver un workflow corriendo ("Build APK") con un círculo
   amarillo. Esperá 2-4 minutos hasta que se ponga verde.
   - Si se pone rojo: hacé clic en el run → clic en "build" →
     se despliega el log. Copiame el error y lo vemos.
3. Con el tilde verde, hacé clic en ese run → abajo de todo vas a ver
   "Artifacts" → descargá **app-debug** (baja un .zip).

## Paso 6 — Instalar en el celular
1. Descomprimí ese .zip: adentro está `app-debug.apk`.
2. Pasáselo a tu celular (por cable, WhatsApp Web, Google Drive, etc.)
3. Al tocarlo para instalar, Android va a pedir permiso para
   "instalar apps de orígenes desconocidos" — lo activás una vez y listo.

## Para actualizar algo después (URL del stream, redes, etc.)
No hace falta repetir todo esto. En la web de GitHub:
1. Buscá el archivo (ej. Constants.kt), abrilo.
2. Lápiz de "editar" arriba a la derecha del archivo.
3. Cambiá el dato, "Commit changes".
4. GitHub Actions compila solo de nuevo — a los pocos minutos tenés
   un nuevo APK esperando en la pestaña Actions.

## Pendiente para más adelante
- Nota de voz en el formulario de mensajes.
- Logo real (reemplazando el ícono placeholder rojo).
- Publicarla en Google Play (eso ya es un trámite aparte con Google,
  con un pago único de cuenta de desarrollador).
