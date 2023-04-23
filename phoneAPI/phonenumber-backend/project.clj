(defproject phonenumber-backend "0.1.0-SNAPSHOT"
  :description "operator mapping api"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure  "1.11.1"]
                 [com.appsflyer/donkey  "0.5.2"]
                 [metosin/reitit        "0.6.0"]
                 [metosin/muuntaja      "0.6.8"]
                 [org.clojure/data.json "2.4.0"]]
  :plugins [[lein-auto "0.1.3"]]
  :main ^:skip-aot phonenumber-backend.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
