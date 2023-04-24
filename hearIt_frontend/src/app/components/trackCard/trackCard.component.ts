import { Component, OnInit } from "@angular/core";
import { UsersService } from "src/app/services/user/user.service";

@Component({
    selector: "app-track-card",
    templateUrl: "./trackCard.component.html",
    styleUrls: ["./trackCard.component.css"],
    inputs: ['track']
})
export class TrackCard implements OnInit {

    public track: any;
    public id: string;
    public name: string;
    public artist: string;
    public popularity: number;
    public album_name: string;

    public image_url: string;

    public isFavorite: boolean = false;

    constructor(private userService: UsersService) {}

    ngOnInit(): void {
        this.id = this.track?.id;
        this.name = this.track?.name;
        this.artist = this.track?.artists.map((art: any) => art.name).join(" & ");
        this.popularity = this.track?.popularity;
        this.album_name = this.track?.album?.name;

        this.image_url = (this.track?.artists[0]?.images) ? this.track?.artists[0]?.images[0]?.url :
            (this.track?.album?.images) ? this.track?.album?.images[0]?.url : '../../assets/images/image_not_available.jpg'; 

        const favoriteIds = JSON.parse(localStorage.getItem("favorite_ids") || '{}')
        this.isFavorite = favoriteIds.includes(this.id);
    }

    addFavorite() {
        const id = {"id": this.id};
        this.userService.addFavoriteTrack(id).subscribe(data => {
            if (data?.status === 204) {
                this.isFavorite = true;
            }
        })
    }

    removeFavorite() {
        const id = {"id": this.id};
        this.userService.removeFavoriteTrack(id).subscribe(data => {
            if (data?.status === 204) {
                this.isFavorite = false;
            }
        })
    }
}