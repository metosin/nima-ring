(ns metosin.nima-ring.http-method-test
  (:require [clojure.test :refer [deftest is]]
            [metosin.nima-ring.http-method :refer [http-method]]))


(deftest http-method-test
  (is (every? (partial instance? io.helidon.common.http.Http$Method)
              (map http-method [:get :post :put :delete :patch :head :options :trace :connect])))
  (is (= io.helidon.common.http.Http$Method/GET
         (http-method :get)))
  (is (= io.helidon.common.http.Http$Method/GET
         (http-method io.helidon.common.http.Http$Method/GET))))
