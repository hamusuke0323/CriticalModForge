package com.hamusuke.critical.server.command;

import com.google.common.primitives.Floats;
import com.hamusuke.critical.invoker.LivingEntityInvoker;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SetCritCommand extends CommandBase {
    @Override
    public String getName() {
        return "setCrit";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setCrit <targets> (critRate | critDamage) <percent>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            List<LivingEntityInvoker> livingEntityInvokers = getEntityList(server, sender, args[0]).stream().filter(entity -> entity instanceof LivingEntityInvoker).map(entity -> (LivingEntityInvoker) entity).collect(Collectors.toList());
            if (livingEntityInvokers.isEmpty()) {
                throw new EntityNotFoundException(args[0]);
            }

            float value = parseFloat(args[2]);

            if (args[1].equalsIgnoreCase("critRate")) {
                livingEntityInvokers.forEach(invoker -> {
                    invoker.setCritRate(value);
                    notifyCommandListener(sender, this, "critical.command.setCritRate.single", ((EntityLivingBase) invoker).getDisplayName(), value);
                });
            } else if (args[1].equalsIgnoreCase("critDamage")) {
                livingEntityInvokers.forEach(invoker -> {
                    invoker.setCritDamage(value);
                    notifyCommandListener(sender, this, "critical.command.setCritDamage.single", ((EntityLivingBase) invoker).getDisplayName(), value);
                });
            }
        }
    }

    private static float parseFloat(String input) throws NumberInvalidException {
        try {
            float f = Float.parseFloat(input);

            if (!Floats.isFinite(f)) {
                throw new NumberInvalidException("commands.generic.num.invalid", input);
            } else {
                return f;
            }
        } catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.num.invalid", input);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
