import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { Observable } from "rxjs";

export const OnlyLoggedInUsersGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const router: Router = inject(Router);
    const cookies: CookieService = inject(CookieService);

    const loggedIn = cookies.check('token');
    if (loggedIn) {
        return true;
    } else {
        router.navigate(['/welcome'])
        return false
    }
}
