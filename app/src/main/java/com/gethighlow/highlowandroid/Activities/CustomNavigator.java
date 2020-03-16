package com.gethighlow.highlowandroid.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator.Name;
import androidx.navigation.fragment.FragmentNavigator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Name("keep_state_fragment")
@Metadata(
        mv = {1, 1, 16},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ0\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"},
        d2 = {"Lcom/gethighlow/highlowandroid/Activities/CustomNavigator;", "Landroidx/navigation/fragment/FragmentNavigator;", "context", "Landroid/content/Context;", "manager", "Landroidx/fragment/app/FragmentManager;", "containerId", "", "(Landroid/content/Context;Landroidx/fragment/app/FragmentManager;I)V", "navigate", "Landroidx/navigation/NavDestination;", "destination", "Landroidx/navigation/fragment/FragmentNavigator$Destination;", "args", "Landroid/os/Bundle;", "navOptions", "Landroidx/navigation/NavOptions;", "navigatorExtras", "Landroidx/navigation/Navigator$Extras;", "app"}
)
public final class CustomNavigator extends FragmentNavigator {
    private final Context context;
    private final FragmentManager manager;
    private final int containerId;

    @Nullable
    public NavDestination navigate(@NotNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Extras navigatorExtras) {
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        String tag = String.valueOf(destination.getId());
        FragmentTransaction var10000 = this.manager.beginTransaction();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "manager.beginTransaction()");
        FragmentTransaction transaction = var10000;
        boolean initialNavigate = false;
        Fragment currentFragment = this.manager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            Intrinsics.checkExpressionValueIsNotNull(transaction.detach(currentFragment), "transaction.detach(currentFragment)");
        } else {
            initialNavigate = true;
        }

        Fragment fragment = this.manager.findFragmentByTag(tag);
        if (fragment == null) {
            String var11 = destination.getClassName();
            Intrinsics.checkExpressionValueIsNotNull(var11, "destination.className");
            String className = var11;
            fragment = this.manager.getFragmentFactory().instantiate(this.context.getClassLoader(), className);
            transaction.add(this.containerId, fragment, tag);
        } else {
            transaction.attach(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();
        return (NavDestination)(initialNavigate ? destination : null);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public NavDestination navigate(NavDestination var1, Bundle var2, NavOptions var3, Extras var4) {
        return this.navigate((Destination)var1, var2, var3, var4);
    }

    public CustomNavigator(@NotNull Context context, @NotNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.context = context;
        this.manager = manager;
        this.containerId = containerId;
    }
}
