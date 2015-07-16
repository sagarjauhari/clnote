FROM clojure

# Update and install applications
RUN apt-get update

# Cache dependencies unless project.clj changes
ADD project.clj /usr/src/clnote/
WORKDIR /usr/src/clnote
RUN lein deps

# Add the entire project
ADD . /usr/src/clnote

# Expose a port for web app
EXPOSE 5001

# Expose a port for nREPL
EXPOSE 7001
