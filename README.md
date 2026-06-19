# Proyecto PawVet - Gestión Veterinaria 🐾

**INTEGRANTES:** [Kevin Quispe Ccolque, Junior Cueva]  
**Temática:** Aplicación para la gestion de una veterinaria , citas de mascotas y consultas veterinarias.

---

## 📱 Capturas de Pantalla

> *Aquí pondré las fotos de mi proyecto funcionando:*

1. **Dashboard Principal:** (Aquí se ve el menú de navegación)  
   ![img.png](img.png)

2. **Gestión de Mascotas (CRUD):** (Aquí muestro cómo agrego y listo mis mascotas)  
   ![img_1.png](img_1.png)

3. **Cita medica ():** (Aquí muestro el form)  
   ![img_3.png](img_3.png)

---

## 🏗️ Aplicación de la Arquitectura MVVM
En este proyecto usamos **MVVM** para que el código no sea un "espagueti".
- **Model:** Usamos Room para las tablas de Mascotas y Retrofit para las de la API.
- **View:** Son nuestras pantallas en Compose que solo muestran lo que el ViewModel les dice.
- **ViewModel:** Es el cerebro que maneja el "UiState". Cuando algo cambia (como agregar una mascota), el ViewModel actualiza el estado y la pantalla se refresca solita (recomposición).

---

## 🤖 Uso de IA y Trabajo Propio

**Trabajo del equipo:**
- La lógica de qué campos pedir en cada formulario.
- **Manejo Offline:** si no hay internet, la app no se cierre y muestre un mensaje de error, algo básico pero que funciona bien.
- La organización de la base de datos local para que las mascotas se guarden aunque apagues el celular.

**Trabajo con ayuda de la IA:**
- Me ayudó a armar la estructura básica de las carpetas y la navegación.
- El chat me guio para corregir errores cuando los datos de la API no cargaban.
- Lo usé para que me explique términos difíciles y limpiar el código que no usaba.


---

**Nota:** Al principio tuvimos problemas conectando el ViewModel con el Repositorio, pero usando el chat logramos entender cómo pasar los datos por el `AppContainer` y así dejar el código ordenado.
