provider "kubernetes" {
  config_path = "/home/taleh/.kube/config"
}

resource "kubernetes_namespace" "ba" {
  metadata {
    annotations = {
      name = "ba-namespace"
    }

    labels = {
      application = "vector-generator"
    }

    name = "ba"
  }
}

resource "kubernetes_deployment" "ba-backend" {
  metadata {
    namespace = "ba"
    name = "ba-backend-deployment"
    labels = {
      app = "backend"
    }
  }
  spec {
    selector {
      match_labels = {
        application = "backend"
      }
    }
    template {
      metadata {
        namespace = "ba"
        name = "backend"
        labels = {
          application = "backend"
        }
      }
      spec {
        container {
          image = "tisserv/ba_backend:latest"
          name = "backend"

          liveness_probe {
            initial_delay_seconds = 10
            period_seconds = 5
            failure_threshold = 10
            http_get {
              path = "/assets"
              port = "8080"
            }
          }

          readiness_probe {
            initial_delay_seconds = 10
            period_seconds = 5
            failure_threshold = 10
            http_get {
              path = "/assets"
              port = "8080"
            }
          }

          port {
            container_port = 8080
          }
        }
      }
    }
    replicas = 2
  }
}

resource "kubernetes_service" "backend" {
  metadata {
    namespace = "ba"
    name = "backend"
  }
  spec {
    selector = {
      application = "${kubernetes_deployment.ba-backend.spec[0].template[0].metadata.0.labels.application}"
    }
    session_affinity = "ClientIP"
    port {
      port = 80
      target_port = 8080
    }

    type = "NodePort"
  }
}

resource "kubernetes_deployment" "ba-ui" {
  metadata {
    namespace = "ba"
    name = "ba-ui-deployment"
    labels = {
      app = "ui"
    }
  }
  spec {
    selector {
      match_labels = {
        application = "ui"
      }
    }
    template {
      metadata {
        namespace = "ba"
        name = "ui"
        labels = {
          application = "ui"
        }
      }
      spec {
        container {
          image = "tisserv/ba_ui:latest"
          name = "ui"

          liveness_probe {
            period_seconds = 1
            http_get {
              path = "/"
              port = "80"
            }
          }

          readiness_probe {
            period_seconds = 1
            http_get {
              path = "/"
              port = "80"
            }
          }

          port {
            container_port = 80
          }
        }
      }
    }
    replicas = 2
  }
}

resource "kubernetes_service" "ui" {
  metadata {
    namespace = "ba"
    name = "ui"
  }
  spec {
    selector = {
      application = "${kubernetes_deployment.ba-ui.spec[0].template[0].metadata.0.labels.application}"
    }
    session_affinity = "ClientIP"
    port {
      port = 80
      target_port = 80
    }

    type = "NodePort"
  }
}

resource "kubernetes_ingress" "ui" {
  metadata {
    name = "ui"
    namespace = "ba"
  }
  spec {
    rule {
      host = "ba.twl.tisserv.net"
      http {
        path {
          path = "/"
          backend {
            service_name = "ui"
            service_port = "80"
          }
        }
      }
    }
    rule {
      host = "ba.tw.tisserv.net"
      http {
        path {
          path = "/"
          backend {
            service_name = "ui"
            service_port = "80"
          }
        }
      }
    }

  }
}