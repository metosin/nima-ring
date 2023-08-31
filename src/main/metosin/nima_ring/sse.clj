(ns metosin.nima-ring.sse
  (:import (io.helidon.nima.sse SseEvent)
           (io.helidon.nima.sse.webserver SseSink)
           (io.helidon.nima.webserver.http ServerResponse)))


(set! *warn-on-reflection* true)


(defn sse-event ^SseEvent [data-or-opts]
  (if (map? data-or-opts)
    (let [builder (SseEvent/builder)]
      (when-let [comment (:comment data-or-opts)]
        (.comment builder comment))
      (when-let [id (:id data-or-opts)]
        (.id builder id))
      (when-let [name (:name data-or-opts)]
        (.name builder name))
      (when-let [data (:data data-or-opts)]
        (.data builder data))
      (.build builder))
    (SseEvent/create data-or-opts)))


(defn- get-sink ^SseSink [req]
  (if (instance? SseSink req)
    req
    (if-let [^ServerResponse resp (:nima/response req)]
      (.sink resp SseSink/TYPE)
      (throw (IllegalArgumentException. (format "can't get sink from request: [%s] (%s)" req (type req)))))))


(defn sse-emitter [req]
  (let [sink (get-sink req)]
    (fn
      ([]
       (try
         (.close sink)
         (catch java.io.UncheckedIOException _)))
      ([event]
       (.emit sink (sse-event event))))))

