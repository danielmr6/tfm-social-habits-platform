FROM node:20

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 3000

ENV CHOKIDAR_USEPOLLING=true
ENV WDS_SOCKET_PORT=0
ENV REACT_APP_API_URL=http://localhost:8080

CMD ["npm", "start"]