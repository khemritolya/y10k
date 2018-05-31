package com.kti.y10k.universe;

public class MaterialAmount {
    public int amount;
    public Material m;

    public enum Material {
        Titanite(20, 10, new MaterialAmount[] {
                new MaterialAmount(Material.Titanium, 6),
                new MaterialAmount(Material.Radioactives, 1)}),
        Skitite(30, 40, new MaterialAmount[] {
                new MaterialAmount(Material.Iron, 4),
                new MaterialAmount(Material.Aluminium, 2),
                new MaterialAmount(Material. Radioactives, 1)}),
        Omisite(50, 60, new MaterialAmount[] {
                new MaterialAmount(Material.Iron, 2),
                new MaterialAmount(Material.Titanium, 2),
                new MaterialAmount(Material.Aluminium, 2),
                new MaterialAmount(Material.Hydrogen, 1)}),
        Trikite(50, 130, new MaterialAmount[] {
                new MaterialAmount(Material.Aluminium, 6),
                new MaterialAmount(Material.Iron, 1),
                new MaterialAmount(Material.Gas, 1)}),
        Gas(2, 70, new MaterialAmount[] {
                new MaterialAmount(Material.Hydrogen, 1)}),
        Iron(10, 4, null),
        Titanium(6, 10, null),
        Aluminium(8, 6, null),
        Radioactives(15, 10, null),
        Hydrogen(1, 50, null);

        public final int massPerUnit;
        public final int volumePerUnit;

        public MaterialAmount[] refinesTo;

        Material(int massPerUnit, int volumePerUnit, MaterialAmount[] refinesTo) {
            this.massPerUnit = massPerUnit;
            this.volumePerUnit = volumePerUnit;
            this.refinesTo = refinesTo;
        }
    }

    public MaterialAmount(Material m, int amount) {
        this.m = m;
        this.amount = amount;
    }
}