# taskTrackerWebflux
приложение «Трекер задач» webFlux+MongoDB

Reactive+MongoDB+Rest  

приложение «Трекер задач», которое работает с двумя сущностями: User и Task.  

1. Запустить docker-compose up  
2. Запустить сервсис, реализованы следующие enpoints  
   - http://localhost:8080/api/v1/users (CRUD)  
   - http://localhost:8080/api/v1/tasks (CRUD)  
   - http://localhost:8080/api/v1/tasks/task-add-observer/{id} (PUT)  
   - http://localhost:8080/api/v1/users/stream   
