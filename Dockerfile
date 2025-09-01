# Étape 1 : Build Angular
FROM node:20 AS build

# Créer un répertoire de travail
WORKDIR /app

# Copier les fichiers package.json et installer les dépendances
COPY package*.json ./
RUN npm install

# Copier le reste du code source et lancer le build
COPY . .
RUN npm run build --prod

# Étape 2 : Servir avec Nginx
FROM nginx:alpine

# Copier le build Angular dans le dossier Nginx
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Exposer le port HTTP
EXPOSE 80

# Lancer Nginx
CMD ["nginx", "-g", "daemon off;"]
