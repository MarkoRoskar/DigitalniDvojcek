FROM node:16.15.0

WORKDIR /app/frontend

COPY package*.json ./

RUN npm install --silent

COPY . .

CMD ["npm" , "start"]