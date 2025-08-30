import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tache } from '../Model/Tache.model';

@Injectable({
  providedIn: 'root'
})
export class TacheService {
private baseURL = "http://localhost:9000/api/B3/tache";
 
   constructor(private httpClient: HttpClient) { }
   
   getEmployeeList(): Observable<Tache[]>{
     return this.httpClient.get<Tache[]>(`${this.baseURL}`);
   }

 


  // 🔄 Update
  updateEmployee(id: number, projet: Tache): Observable<Tache> {
    return this.httpClient.put<Tache>(`${this.baseURL}/${id}`, projet);
  }

  // 🗑️ Supprimer 
  deleteEmployee(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${id}`);
  }

  getEmployeeById(id: number): Observable<any> {
  return this.httpClient.get(`${this.baseURL}/${id}`);
}
getTachesByProjet(projetId: number): Observable<any[]> {
  return this.httpClient.get<any[]>(`${this.baseURL}/projet/${projetId}`);
}

addTache(projetId: number, tache: any): Observable<any> {
  const token = localStorage.getItem('accessToken'); // ou là où vous stockez le JWT
  return this.httpClient.post(
    `${this.baseURL}/ajouter/${projetId}`, 
    tache,
    {
      headers: { Authorization: `Bearer ${token}` }
    }
  );
}
}


