(require '[buddy.auth.backends :as backends])

(defn my-authfn
  [request authdata]
  (let [username (:username authdata)
        password (:password authdata)]
    username))

(def backend (backends/basic {:realm "MyApi"
                              :authfn my-authfn}))
