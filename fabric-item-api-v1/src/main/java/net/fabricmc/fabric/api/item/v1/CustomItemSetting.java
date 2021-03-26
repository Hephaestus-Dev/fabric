/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.item.v1;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.item.Item;

import net.fabricmc.fabric.impl.item.CustomItemSettingImpl;

/**
 * A type of setting that can be passed to item constructors.
 * This feature can be used by mods to add non-standard settings
 * to items in a way that is compatible with other mods that add
 * settings to items.
 *
 * <p>Values of this setting can be retrieved from an item using {@link CustomItemSetting#getValue(Item)},
 * and users that wish to expose a custom setting for use in other mods should do so by exposing
 * the CustomItemSetting instance.
 *
 * <h3>Usage Example</h3>
 * <pre>{@code
 * // Create the setting instance. You can think of this as the "setting key".
 * public static final CustomItemSetting<String> CUSTOM_TOOLTIP = CustomItemSetting.create(() -> null);
 *
 * @Override
 * public void onInitializeClient() {
 *     ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
 *         // Gets the setting from the specified item.
 *         String tooltip = CUSTOM_TOOLTIP.getValue(stack.getItem());
 *
 *         if (tooltip != null) {
 *             lines.add(new LiteralText(tooltip));
 *         }
 *     }
 * }}</pre>
 *
 * <p>You should probably not implement this interface, unless you have a very highly specialized use case.
 *
 * <p>Use {@link CustomItemSetting#create(Supplier)} to retrieve an instance of Fabric API's default implementation.
 *
 * @param <T> the type of the setting to be attached
 */
public interface CustomItemSetting<T> {
	/**
	 * Returns the current value of this setting for the given {@link Item}.
	 * Should only be called after or within item construction.
	 *
	 * @param item the item
	 * @return the current setting if present, the default setting if not
	 */
	T getValue(Item item);

	/**
	 * Sets the value of this CustomItemSetting on the given {@link Item.Settings} instance.
	 *
	 * @param settings the item settings to modify
	 * @param value the updated value of this setting
	 */
	void set(@NotNull Item.Settings settings, T value);

	/**
	 * Signals to this CustomItemSetting to associate its value in the {@link Item.Settings} instance with an Item.
	 *
	 * <p>Should really only be called by the implementation. Is exposed for people who need highly specialized settings
	 *
	 * @param settings the item settings to draw a value from
	 * @param item the item to have its value set
	 */
	void build(@NotNull Item.Settings settings, Item item);

	/**
	 * Creates a new CustomItemSetting with the given default value.
	 *
	 * @param defaultValue the value all items that do not explicitly set this setting will have.
	 * @return a new CustomItemSetting
	 */
	static <T> CustomItemSetting<T> create(Supplier<T> defaultValue) {
		return new CustomItemSettingImpl<>(defaultValue);
	}
}