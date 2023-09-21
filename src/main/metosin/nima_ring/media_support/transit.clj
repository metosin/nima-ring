(ns metosin.nima-ring.media-support.transit
  (:require [cognitect.transit :as transit]
            [metosin.nima-ring.http-header :refer [http-header-value]]
            [metosin.nima-ring.media-type :refer [media-type]]
            [metosin.nima-ring.media-support :as ms])
  (:import (io.helidon.common.http WritableHeaders)
           (io.helidon.nima.http.media MediaSupport)))


(set! *warn-on-reflection* true)


;;
;; ==================================================================================================
;; Transit
;; ==================================================================================================
;;


(def application-transit+json "application/transit+json")
(def media-type-transit (media-type application-transit+json))
(def content-type-transit (http-header-value "content-type" (str application-transit+json "; charset=UTF-8")))


(defn transit-media-support ^MediaSupport []
  (ms/simple-media-support
   media-type-transit
   (ms/reader-response :supported (fn [_ ^java.io.InputStream in _]
                                    (-> (transit/reader in :json)
                                        (transit/read))))
   (ms/writer-response :supported (fn [_ body ^java.io.OutputStream out _ ^WritableHeaders response-headers]
                                    (.setIfAbsent response-headers content-type-transit)
                                    (-> (transit/writer out :json)
                                        (transit/write body))))))
