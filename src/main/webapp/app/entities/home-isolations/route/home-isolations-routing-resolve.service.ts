import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHomeIsolations, HomeIsolations } from '../home-isolations.model';
import { HomeIsolationsService } from '../service/home-isolations.service';

@Injectable({ providedIn: 'root' })
export class HomeIsolationsRoutingResolveService implements Resolve<IHomeIsolations> {
  constructor(protected service: HomeIsolationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHomeIsolations> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((homeIsolations: HttpResponse<HomeIsolations>) => {
          if (homeIsolations.body) {
            return of(homeIsolations.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HomeIsolations());
  }
}
