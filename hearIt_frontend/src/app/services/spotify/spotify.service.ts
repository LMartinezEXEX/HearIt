import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { Observable } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class SpotifyService {

    private spotify_api = 'https://accounts.spotify.com/';

    private search_retrieve_amount = 20;

    private credentials = {
        clientId: 'f7d4f571dfd34d208eb58310338ee6c3',
        clientSecret: '6d2ed971bd184b7699154f2c0e177045',
        accessToken: ''
    };

    private headers = new HttpHeaders({
        'Authorization': 'Basic ' + btoa(this.credentials.clientId+":"+this.credentials.clientSecret),
        'Content-Type': 'application/x-www-form-urlencoded'
    });

    constructor(private http: HttpClient, private cookies: CookieService) {}

    getUserCode() {
        window.location.href= this.spotify_api + 'es-ES/authorize?client_id=' +
                                this.credentials.clientId + '&response_type=code' +
                                '&redirect_uri=' + encodeURIComponent('http://localhost:4200/home')
    }

    getAccessToken(code: string): Observable<any> {
        const data = {
            'grant_type': 'client_credentials',
            'code': code,
            'redirect_uri': encodeURIComponent('http://localhost:4200/home')
        };

        return this.http.post(this.spotify_api + 'api/token', 'grant_type=client_credentials', {
            headers: this.headers
        })
    }

    search(q: string): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.cookies.get('token'),
            'spotify_access_token': this.cookies.get('spotify_access_token')
        });

        return this.http.get("http://localhost:8080/api/v1/spotify/search?q=" + q + "&amount=" + this.search_retrieve_amount, 
        {
            headers: headers
        })
    }
}