import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HomeIsolationsComponent } from '../list/home-isolations.component';
import { HomeIsolationsDetailComponent } from '../detail/home-isolations-detail.component';
import { HomeIsolationsUpdateComponent } from '../update/home-isolations-update.component';
import { HomeIsolationsRoutingResolveService } from './home-isolations-routing-resolve.service';

const homeIsolationsRoute: Routes = [
  {
    path: '',
    component: HomeIsolationsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HomeIsolationsDetailComponent,
    resolve: {
      homeIsolations: HomeIsolationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HomeIsolationsUpdateComponent,
    resolve: {
      homeIsolations: HomeIsolationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HomeIsolationsUpdateComponent,
    resolve: {
      homeIsolations: HomeIsolationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(homeIsolationsRoute)],
  exports: [RouterModule],
})
export class HomeIsolationsRoutingModule {}
