const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const logger = require("firebase-functions/logger");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue } = require("firebase-admin/firestore");
const { getMessaging } = require("firebase-admin/messaging");

initializeApp();
const db = getFirestore();
const messaging = getMessaging();

exports.sendPushOnNewCita = onDocumentCreated(
  {
    document: "users/{userId}/citas/{citaId}",
    region: "us-central1"
  },
  async (event) => {
    const snapshot = event.data;
    const userId = event.params.userId;
    const citaId = event.params.citaId;

    if (!snapshot) {
      logger.error("Trigger sin snapshot", { userId, citaId });
      return;
    }

    const cita = snapshot.data() || {};
    logger.info("Nueva cita detectada", {
      userId,
      citaId,
      cita
    });

    try {
      const userDoc = await db.collection("users").doc(userId).get();

      if (!userDoc.exists) {
        logger.error("No existe el documento del usuario", { userId, citaId });
        return;
      }

      const userData = userDoc.data() || {};
      const fcmToken = userData.fcmToken;

      if (!fcmToken) {
        logger.warn("Usuario sin token FCM, no se enviara push", {
          userId,
          citaId
        });
        return;
      }

      const tipo = cita.tipo || "Cita medica";
      const fecha = cita.fecha || "";
      const hora = cita.hora || "";

      const message = {
        token: fcmToken,
        notification: {
          title: "PawVet: cita registrada",
          body: `${tipo} agendada para ${fecha} ${hora}`.trim()
        },
        data: {
          title: "PawVet: cita registrada",
          body: `${tipo} agendada para ${fecha} ${hora}`.trim(),
          route: "citas",
          citaId: String(citaId),
          userId: String(userId),
          tipo: String(tipo),
          fecha: String(fecha),
          hora: String(hora)
        },
        android: {
          priority: "high",
          notification: {
            channelId: "pawvet_reminders",
            clickAction: "OPEN_CITAS"
          }
        }
      };

      const response = await messaging.send(message);

      logger.info("Push enviada correctamente", {
        userId,
        citaId,
        response
      });
    } catch (error) {
      logger.error("Error enviando push automatica por nueva cita", {
        userId,
        citaId,
        errorMessage: error.message,
        stack: error.stack
      });

      if (
        error.code === "messaging/registration-token-not-registered" ||
        error.code === "messaging/invalid-registration-token"
      ) {
        await db.collection("users").doc(userId).set(
          { fcmToken: FieldValue.delete() },
          { merge: true }
        );
        logger.warn("Token FCM invalido eliminado del usuario", { userId, citaId });
      }
    }
  }
);
