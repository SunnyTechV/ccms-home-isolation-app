import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HomeIsolationsComponent } from './list/home-isolations.component';
import { HomeIsolationsDetailComponent } from './detail/home-isolations-detail.component';
import { HomeIsolationsUpdateComponent } from './update/home-isolations-update.component';
import { HomeIsolationsDeleteDialogComponent } from './delete/home-isolations-delete-dialog.component';
import { HomeIsolationsRoutingModule } from './route/home-isolations-routing.module';

@NgModule({
  imports: [SharedModule, HomeIsolationsRoutingModule],
  declarations: [
    HomeIsolationsComponent,
    HomeIsolationsDetailComponent,
    HomeIsolationsUpdateComponent,
    HomeIsolationsDeleteDialogComponent,
  ],
  entryComponents: [HomeIsolationsDeleteDialogComponent],
})
export class HomeIsolationsModule {}
