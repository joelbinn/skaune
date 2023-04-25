/*
 * - "DSL" - "DSL" (https://github.com/structurizr/dsl)
*/

workspace "Skaune" "Detta är en modell för Skaune" {
    !docs docs
    !adrs adrs

    model {
        adminUser = person "Systemadminastratör" "Använder applikationen för att hantera Skaune"

        enterprise "Skaune" {
            skaune = softwareSystem "Skaune" "Skaune är en applikation för att hantera Skaune" {
                apiGateway = container "API Gateway" "Edge service"
                dropboxGateway = container "Dropbox Gateway" "Gateway to Dropbox" "downstream-gateway" {
                    dropboxController = component "DropboxController" "REST Controller that accesses Dropbox"
                }

                userInterface = container "User Interface" "User Interface" "SPA"
            }
        }

        dropbox = softwareSystem "Dropbox" "Dropbox SaaS" "external-system"

        adminUser -> userInterface
        userInterface -> apiGateway
        apiGateway -> dropboxController
        dropboxGateway -> dropbox
    }

    views {
        systemlandscape {
            include *
            //autoLayout
        }

        systemcontext skaune {
            include *
            //autoLayout
        }

        container skaune {
            include *
            //autoLayout
        }

        component apiGateway {
            include *
            //autoLayout
        }

        component userInterface {
            include *
            //autoLayout
        }

        component dropboxGateway {
            include *
            //autoLayout
        }

        theme default
    }
}
