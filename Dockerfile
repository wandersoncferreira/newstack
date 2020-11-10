from clojure:openjdk-11-tools-deps

WORKDIR /app

COPY . /app

ENTRYPOINT ["clojure", \
            "-Acider", \
            "-m", "nrepl.cmdline", \
            "-b", "0.0.0.0", \
            "-p", "17020", \
            "--middleware", "[refactor-nrepl.middleware/wrap-refactor,cider.nrepl/cider-middleware]"]
