import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Observable } from "rxjs";
import { environment } from "src/environment/environment";

@Injectable({
    providedIn: "root"
})
export class UsersService {

    api: string = environment.apiUrl;

    constructor(private http: HttpClient, private cookies: CookieService) {}

    login(user: any): Observable<any> {
        return this.http.post(this.api + "auth/login", user);
    }

    register(user: any): Observable<any> {
        return this.http.post(this.api + "auth/register", user);
    }

    getAllTracks(): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.cookies.get('token'),
            'spotify_access_token': this.cookies.get('spotify_access_token')
        });

        return this.http.get(this.api + "user/tracks", {
            headers: headers
        })
    }

    addFavoriteTrack(id: any): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.cookies.get('token')
        });

        return this.http.put(this.api + "user/tracks", id, {
            observe: 'response',
            headers: headers
        })
    }

    removeFavoriteTrack(id: any): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.cookies.get('token')
        });

        return this.http.delete(this.api + "user/tracks", {
            body: id,
            observe: 'response',
            headers: headers
        })
    }

    setId(id: number): void {
        this.cookies.set("us_id", String(id));
    }

    setToken(token: string): void {
        this.cookies.set("token", token);
    }

    getToken(): string {
        return this.cookies.get("token");
    }

    getId(): string {
        return this.cookies.get("us_id");
    }

    logout(): void {
        this.cookies.delete("token");
        this.cookies.delete("us_id");
    }
}