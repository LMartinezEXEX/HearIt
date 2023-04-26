import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router, RoutesRecognized } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { FormControl, FormGroup } from "@angular/forms";
import { debounceTime, distinctUntilChanged, filter, switchMap } from "rxjs";
import { SpotifyService } from "src/app/services/spotify/spotify.service";
import { UsersService } from "src/app/services/user/user.service";

@Component({
    selector: "app-home",
    templateUrl: "./home.component.html",
    styleUrls: ["./home.component.css"]
})
export class HomeComponent implements OnInit{

    public trackList: Array<any> = [];

    searchForm: FormGroup = new FormGroup({
        search: new FormControl('')
    })

    constructor (
        private route: ActivatedRoute, 
        private cookies: CookieService, 
        private router: Router,
        private spotifyService: SpotifyService, 
        private userService: UsersService
    ) {
        
        this.route.queryParams.subscribe(params => {
            const code = params['code']

            if (code) {
                spotifyService.getAccessToken(code).subscribe( data => {
                    this.cookies.set('spotify_access_token', data?.access_token, data?.expires_in);
                })
                this.userService.saveSpotifyCode({'spotifyCode': code}).subscribe();
                this.router.navigate(['/home']);
            }
        });

        this.searchForm
        .get('search')
        ?.valueChanges.pipe(
            debounceTime(300),
            distinctUntilChanged(),
            filter(q => q !== ''),
            switchMap((q) => this.spotifyService.search(q))
        )        
        .subscribe(
            (data) => {
                this.trackList = data?.tracks;
            }
        )
    }

    ngOnInit(): void {
        this.userService.getAllTracks().subscribe( data => {
            this.trackList = (data) ? data?.tracks : [];
            localStorage.setItem("favorite_ids", JSON.stringify(this.trackList.map(track => track?.id)));
        })
    }

    logOut() {
        this.userService.logout();
        this.router.navigateByUrl('/welcome')
    }
}