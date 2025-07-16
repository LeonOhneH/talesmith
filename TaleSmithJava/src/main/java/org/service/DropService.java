package main.java.org.service;

import main.java.org.Item;
import main.java.org.Weapon;
import main.java.org.Consumeable;
import main.java.org.templates.ItemTemplate;
import main.java.org.templates.WeaponTemplate;
import main.java.org.templates.ConsumeableTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DropService {
    private Random random = new Random();

    public List<Item> generateDrops(HashMap<ItemTemplate, Float> possibleDrops) {
        List<Item> drops = new ArrayList<>();

        if (possibleDrops == null || possibleDrops.isEmpty()) {
            return drops;
        }

        for (HashMap.Entry<ItemTemplate, Float> entry : possibleDrops.entrySet()) {
            ItemTemplate template = entry.getKey();
            Float dropChance = entry.getValue();

            if (random.nextFloat() < dropChance) {
                Item item = createItemFromTemplate(template);
                if (item != null) {
                    drops.add(item);
                }
            }
        }

        return drops;
    }

    private Item createItemFromTemplate(ItemTemplate template) {
        if (template instanceof WeaponTemplate) {
            WeaponTemplate weaponTemplate = (WeaponTemplate) template;
            return new Weapon(
                    weaponTemplate.getName(),
                    weaponTemplate.getDescription(),
                    weaponTemplate.getDamage(),
                    weaponTemplate.getType());
        } else if (template instanceof ConsumeableTemplate) {
            ConsumeableTemplate consumeableTemplate = (ConsumeableTemplate) template;
            return new Consumeable(
                    consumeableTemplate.getName(),
                    consumeableTemplate.getDescription());
        }
        return null;
    }
}
