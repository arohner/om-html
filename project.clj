(defproject arohner/om-html "0.1.4-SNAPSHOT"
  :description "hiccup for Om Next"
  :url "http://github.com/arohner/om-html"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]]
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :creds :gpg}]]
  :jvm-opts ["-Xmx1024m"
             "-XX:-OmitStackTraceInFastThrow"
             "-XX:+UseConcMarkSweepGC"
             "-Dfile.encoding=UTF-8"])
