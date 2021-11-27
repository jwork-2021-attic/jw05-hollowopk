/*
 * Copyright (C) 2015 Winterstorm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package world;

/**
 *
 * @author Winterstorm
 */
public class MedicineAI extends ItemAI {

    private static int count = 0;
    public static int maxCount = 10;
    public static double spreadChance = 0.5;

    public MedicineAI(Item item, Factory factory) {
        super(item);
        this.factory = factory;
        count++;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (item.isExist()) {
            try {
                Thread.sleep(5000);
                spread();
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void onDelete() {
        count = Math.max(0, count - 1);
        super.onDelete();
    }

    @Override
    public void spread() {
        synchronized (this.getClass()) {
            if (count < maxCount && Math.random() < spreadChance) {
                factory.newMedicine();
            }
        }
    }

}
