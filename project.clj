(defproject arohner/om-html "0.1.0"
  :description "server-side rendering for Om"
  :url "http://github.com/arohner/foam"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :creds :gpg}]]
  :jvm-opts ["-Xmx1024m"
             "-XX:-OmitStackTraceInFastThrow"
             "-XX:+UseConcMarkSweepGC"
             "-Dfile.encoding=UTF-8"])
