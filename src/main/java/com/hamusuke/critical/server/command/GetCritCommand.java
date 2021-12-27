package com.hamusuke.critical.server.command;

import com.hamusuke.critical.invoker.LivingEntityInvoker;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GetCritCommand extends CommandBase {
    @Override
    public String getName() {
        return "getCrit";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/getCrit <targets> (critRate | critDamage)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(this.getUsage(sender));
        } else {
            List<LivingEntityInvoker> livingEntityInvokers = getEntityList(server, sender, args[0]).stream().filter(entity -> entity instanceof LivingEntityInvoker).map(entity -> (LivingEntityInvoker) entity).collect(Collectors.toList());
            if (livingEntityInvokers.isEmpty()) {
                throw new EntityNotFoundException(args[0]);
            }

            if (args[1].equalsIgnoreCase("critRate")) {
                livingEntityInvokers.forEach(invoker -> sender.sendMessage(new TextComponentTranslation("critical.command.getCritRate", ((EntityLivingBase) invoker).getDisplayName(), invoker.getTotalCritRate())));
            } else if (args[1].equalsIgnoreCase("critDamage")) {
                livingEntityInvokers.forEach(invoker -> sender.sendMessage(new TextComponentTranslation("critical.command.getCritDamage", ((EntityLivingBase) invoker).getDisplayName(), invoker.getTotalCritDamage())));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
